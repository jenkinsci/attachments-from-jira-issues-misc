package org.jenkinsci.plugins.openshift;

import com.openshift.client.IApplication;
import com.openshift.client.IHttpClient.ISSLCertificateCallback;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.remoting.VirtualChannel;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jenkinsci.plugins.openshift.OpenShiftV2Client.DeploymentType;
import org.jenkinsci.plugins.openshift.OpenShiftV2Client.ValidationResult;
import org.jenkinsci.plugins.openshift.util.JenkinsLogger;
import org.jenkinsci.plugins.openshift.util.Utils;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.net.ssl.SSLSession;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import static org.jenkinsci.plugins.openshift.util.Utils.*;

/**
 * @author Siamak Sadeghianfar <ssadeghi@redhat.com>
 */
public class DeployApplication extends Builder implements BuildStep {
	private static final String WORK_DIR = "/openshift-deployer-workdir";

	private static final String BINARY_TAR_NAME = "app.tar.gz";

	private static final Logger LOG = Logger.getLogger(DeployApplication.class
			.getName());

	private String serverName;
	private String cartridges;
	private String domain;
	private String gearProfile;
	private String appName;
	private String deploymentPackage;
	private String environmentVariables;
	private Boolean autoScale;
	private DeploymentType deploymentType = DeploymentType.GIT;
	private String openshiftDirectory;

	@DataBoundConstructor
	public DeployApplication(String serverName, String appName,
			String cartridges, String domain, String gearProfile,
			String deploymentPackage, String environmentVariables,
			Boolean autoScale, DeploymentType deploymentType,
			String openshiftDirectory) {
		this.serverName = serverName;
		this.appName = appName;
		this.cartridges = cartridges;
		this.domain = domain;
		this.gearProfile = gearProfile;
		this.deploymentPackage = deploymentPackage;
		this.environmentVariables = environmentVariables;
		this.autoScale = autoScale;
		this.deploymentType = deploymentType;
		this.openshiftDirectory = openshiftDirectory;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		if (build != null && build.getResult() != null
				&& build.getResult().isWorseThan(Result.SUCCESS)) {
			abort(listener, "Build is not success : will not try to deploy.");
		}

		if (isEmpty(appName)) {
			abort(listener, "Application name is not specified.");
		}

		if (isEmpty(cartridges)) {
			abort(listener, "Cartridges are not specified.");
		}

		if (isEmpty(deploymentPackage)) {
			abort(listener, "Deployment path is not specified.");
		}

		try {
			// find deployment unit
			List<String> deployments = findDeployments(build, listener);

			if (deployments.isEmpty()) {
				abort(listener, "No packages found to deploy to OpenShift.");
			} else {
				log(listener, "Deployments found: " + deployments);
			}

			Server server = findServer(serverName);
			if (server == null) {
				abort(listener,
						"No OpenShift server is selected or none are defined in Jenkins Configuration.");
			}

			log(listener,
					"Deploying to OpenShift at http://"
							+ server.getBrokerAddress()
							+ ". Be patient! It might take a minute...");

			OpenShiftV2Client client = new OpenShiftV2Client(
					server.getBrokerAddress(), server.getUsername(),
					server.getPassword());

			String targetDomain = domain;
			if (isEmpty(targetDomain)) { // pick the domain if only one exists
				List<String> domains = client.getDomains();

				if (domains.size() > 1) {
					abort(listener,
							"Specify the user domain. " + domains.size()
									+ " domains found on the account.");
				} else if (domains.isEmpty()) {
					abort(listener, "No domains exist. Create a domain first.");
				}

				targetDomain = domains.get(0);
			}

			IApplication app;
			if (isEmpty(environmentVariables)) {
				app = client.getOrCreateApp(appName, targetDomain,
						Arrays.asList(cartridges.split(" ")), gearProfile,
						null, autoScale);
			} else {
				Map<String, String> mapOfEnvironmentVariables = new HashMap<String, String>();
				for (String environmentVariable : Arrays
						.asList(environmentVariables.split(" "))) {
					if (environmentVariable.contains("=")) {
						String[] parts = environmentVariable.split("=");
						mapOfEnvironmentVariables.put(parts[0], parts[1]);
					} else {
						abort(listener, "Invalid environment variable: "
								+ environmentVariable);
					}
				}
				app = client.getOrCreateApp(appName, targetDomain,
						Arrays.asList(cartridges.split(" ")), gearProfile,
						mapOfEnvironmentVariables, autoScale);
			}

			deploy(deployments, app, build, listener);

		} catch (Exception e) {
			abort(listener, e);
		}

		return true;
	}

	private void deploy(List<String> deployments, IApplication app,
			AbstractBuild<?, ?> build, BuildListener listener)
			throws GitAPIException, IOException {
		if (deployments == null || deployments.isEmpty()) {
			abort(listener, "Deployment package list is empty.");
		}

		if (deploymentType == DeploymentType.BINARY) {
			doBinaryDeploy(deployments.get(0), app, build, listener);
		} else {
			doGitDeploy(deployments, app, build, listener);
		}

		log(listener, "Application deployed to " + app.getApplicationUrl());
	}

	private void doBinaryDeploy(String deployment, IApplication app,
			AbstractBuild<?, ?> build, final BuildListener listener)
			throws IOException {
		// reconfigure app for binary deploy
		if (!app.getDeploymentType().equalsIgnoreCase(
				DeploymentType.BINARY.name())) {
			app.setDeploymentType(DeploymentType.BINARY.toString()
					.toLowerCase());
		}

		// deploy
		SSHClient sshClient = new SSHClient(app);
		sshClient.setLogger(new JenkinsLogger(listener));
		sshClient.setSSHPrivateKey(Utils.getSSHPrivateKey());
		sshClient.deploy(getBinaryDeploymentFile(build, deployment, listener));
	}

	private File getBinaryDeploymentFile(AbstractBuild<?, ?> build,
			String deployment, BuildListener listener) throws IOException {
		if (isURL(deployment)) {
			File baseDir = createBaseDir(build);
			File dest = new File(baseDir.getAbsolutePath() + "/"
					+ BINARY_TAR_NAME);
			LOG.fine("Downloading the deployment binary to '"
					+ dest.getAbsolutePath() + "'");
			copyURLToFile(new URL(deployment), dest, 10000, 10000);
			return dest;

		} else {
			File baseDir = createBaseDir(build);
			File dest = new File(baseDir.getAbsolutePath() + "/"
					+ BINARY_TAR_NAME);
			log(listener,
					"Creating local file for deployment: "
							+ dest.getAbsolutePath());
			FilePath remote = new FilePath(build.getWorkspace().getChannel(),
					deployment);
			FilePath destFile = new FilePath(dest);
			try {
				log(listener,
						"Copying deployment file to local file: "
								+ dest.getAbsolutePath());
				log(listener, "    Source: " + remote);
				log(listener, "    Target: " + dest.getAbsolutePath());
				remote.copyTo(destFile);
			} catch (InterruptedException e1) {
				abort(listener,
						"Exception copying file to master host: "
								+ e1.getMessage());
			}
			return dest;
		}
	}

	private void doGitDeploy(List<String> deployments, IApplication app,
			AbstractBuild<?, ?> build, BuildListener listener)
			throws GitAPIException, IOException {
		File baseDir = createBaseDir(build);
		String commitMsg = "deployment added for Jenkins build "
				+ build.getDisplayName() + "#" + build.getNumber();

		String relativeDeployPath;
		if (cartridges.contains("jbossews")) {
			relativeDeployPath = "/webapps"; // tomcat
		} else {
			relativeDeployPath = "/deployments"; // jboss/wildfly
		}

		String dotOpenshiftDirectory = null;
		if (!StringUtils.isEmpty(openshiftDirectory)) {
			if (new File(openshiftDirectory).isAbsolute())
				dotOpenshiftDirectory = openshiftDirectory;
			else
				dotOpenshiftDirectory = build.getWorkspace() + File.separator
						+ openshiftDirectory;
		}
		GitClient gitClient = new GitClient(app);
		gitClient.setLogger(new JenkinsLogger(listener));
		gitClient.deploy(deployments, baseDir, relativeDeployPath, commitMsg,
				dotOpenshiftDirectory);
	}

	private File createBaseDir(AbstractBuild<?, ?> build) throws IOException {
		File baseDir = new File(build.getWorkspace() + WORK_DIR);
		if (baseDir.exists()) {
			FileUtils.deleteDirectory(baseDir);
		}

		baseDir.mkdirs();
		return baseDir;
	}

	@SuppressWarnings("deprecation")
	private List<String> findDeployments(AbstractBuild<?, ?> build,
			BuildListener listener) throws AbortException {
		List<String> deployments = new ArrayList<String>();

		if (isURL(deploymentPackage)) {
			try {
				deployments.add(TokenMacro.expand(build, listener,
						deploymentPackage));
			} catch (Exception e) {
				throw new AbortException(e.getMessage());
			}

		} else {

			String filePath = build.getWorkspace() + File.separator
					+ deploymentPackage;
			VirtualChannel channel = build.getWorkspace().getChannel();

			log(listener,
					"Using hudson.FilePath for resolving content for deploy:\n    Channel: "
							+ channel + " \n    FilePath: " + filePath);
			FilePath dir = new FilePath(channel, filePath);

			try {
				if (!dir.exists()) {
					abort(listener, "Directory '" + dir
							+ "' doesn't exist. No deployments found!");
				}
			} catch (IOException e) {
				throw new AbortException(e.getMessage());
			} catch (InterruptedException e) {
				throw new AbortException(e.getMessage());
			}

			// Let us handle directories
			try {
				if (dir.isDirectory()) {

					String includes = null;
					if (deploymentType == DeploymentType.BINARY) {
						includes = "*.tar.gz";
					} else {
						includes = "*.ear,*.war";
					}

					FilePath[] deploymentFiles = dir.list(includes);
					for (FilePath file : deploymentFiles) {
						deployments.add(file.toString());
						log(listener, "Adding " + file.toString()
								+ " to Deployment List");
					}
				}
				// Handle single Files
				else if (!dir.isDirectory()
						&& (dir.toString().toLowerCase().endsWith(".ear") || dir
								.toString().toLowerCase().endsWith(".war"))) {
					deployments.add(dir.toString());
					log(listener, "Adding " + dir.toString()
							+ " to Deployment List");
				}
			} catch (IOException e) {
				throw new AbortException(e.getMessage());
			} catch (InterruptedException e) {
				throw new AbortException(e.getMessage());
			}
		}

		// If we cannot find any deployments we should abort to avoid
		// NullPointers
		if (deployments.isEmpty()) {
			abort(listener, "No Deployments found! (configuredValue: "
					+ deploymentPackage + ")");
		}

		return deployments;
	}

	public boolean isBinaryDeploy() {
		return deploymentType == DeploymentType.BINARY;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

	public String getServerName() {
		return serverName;
	}

	public String getCartridges() {
		return cartridges;
	}

	public String getDomain() {
		return domain;
	}

	public String getGearProfile() {
		return gearProfile;
	}

	public String getAppName() {
		return appName;
	}

	public String getDeploymentPackage() {
		return deploymentPackage;
	}

	public String getEnvironmentVariables() {
		return environmentVariables;
	}

	public Boolean autoScale() {
		return autoScale;
	}

	public DeploymentType getDeploymentType() {
		return deploymentType;
	}

	public static class TrustingISSLCertificateCallback implements
			ISSLCertificateCallback {
		public boolean allowCertificate(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean allowHostname(String hostname, SSLSession session) {
			return true;
		}
	}

	@Extension
	public static class DeployApplicationDescriptor extends AbstractDescriptor {
		private final String DEFAULT_PUBLICKEY_PATH = System
				.getProperty("user.home") + "/.ssh/id_rsa.pub";

		private List<Server> servers = new ArrayList<Server>();

		public String publicKeyPath;

		public DeployApplicationDescriptor() {
			super(DeployApplication.class);
			load();
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject json)
				throws FormException {
			Object s = json.get("servers");
			if (!JSONNull.getInstance().equals(s)) {
				servers = req.bindJSONToList(Server.class, s);
			} else {
				servers = null;
			}

			publicKeyPath = json.getString("publicKeyPath");
			save();
			return super.configure(req, json);
		}

		@Override
		public String getDisplayName() {
			return Utils.getBuildStepName("Deploy Application");
		}

		public List<Server> getServers() {
			return servers;
		}

		public String getPublicKeyPath() {
			return isEmpty(publicKeyPath) ? DEFAULT_PUBLICKEY_PATH
					: publicKeyPath;
		}

		public FormValidation doCheckLogin(
				@QueryParameter("brokerAddress") final String brokerAddress,
				@QueryParameter("username") final String username,
				@QueryParameter("password") final String password) {
			OpenShiftV2Client client = new OpenShiftV2Client(brokerAddress,
					username, password);
			ValidationResult result = client.validate();
			if (result.isValid()) {
				return FormValidation.ok("Success");
			} else {
				return FormValidation.error(result.getMessage());
			}
		}

		public FormValidation doUploadSSHKeys(
				@QueryParameter("brokerAddress") final String brokerAddress,
				@QueryParameter("username") final String username,
				@QueryParameter("password") final String password,
				@QueryParameter("publicKeyPath") final String publicKeyPath) {
			OpenShiftV2Client client = new OpenShiftV2Client(brokerAddress,
					username, password);

			try {
				if (publicKeyPath == null) {
					return FormValidation
							.error("Specify the path to SSH public key.");
				}
				File file = new File(publicKeyPath);

				if (!file.exists()) {
					return FormValidation
							.error("Specified SSH public key doesn't exist: "
									+ publicKeyPath);
				}

				if (client.sshKeyExists(file)) {
					return FormValidation.ok("SSH public key already exists.");

				} else {
					client.uploadSSHKey(file);
					return FormValidation
							.ok("SSH Public key uploaded successfully.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				return FormValidation.error(e.getMessage());
			}
		}

		public ListBoxModel doFillGearProfileItems(
				@QueryParameter("serverName") final String serverName) {
			ListBoxModel items = new ListBoxModel();
			Server server = findServer(serverName);

			if (server == null) {
				return items;
			}

			OpenShiftV2Client client = new OpenShiftV2Client(
					server.getBrokerAddress(), server.getUsername(),
					server.getPassword());
			for (String gearProfile : client.getGearProfiles()) {
				items.add(gearProfile, gearProfile);
			}

			return items;
		}

		public FormValidation doCheckPublicKeyPath(
				@QueryParameter("publicKeyPath") String path) {
			File file = new File(path);

			if (!file.exists()) {
				return FormValidation.error("Public key doesn't exist at "
						+ path);
			}

			return FormValidation.ok();
		}
	}
}

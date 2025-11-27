package hudson;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildBadgeAction;
import hudson.model.TaskListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class CustomFileSystemProvisioner extends FileSystemProvisioner
{
    public static final FileSystemProvisioner RELEASE = new CustomFileSystemProvisioner();    

	protected boolean isReleaseBuild(AbstractBuild<?, ?> build)
	{
		for (BuildBadgeAction action : build.getBadgeActions())
		{
			if (action.getClass().getName().equals("hudson.plugins.release.ReleaseWrapper$ReleaseBuildBadgeAction"))
			{
				return true;
			}
		}
		return false;
	}
	
    public void prepareWorkspace(AbstractBuild<?, ?> build, FilePath ws, TaskListener listener) throws IOException, InterruptedException {
    	if (isReleaseBuild(build))
    	{
    		listener.getLogger().println("Cleaning workspace...");
    		ws.deleteRecursive();
    	}
    }

    public void discardWorkspace(AbstractProject<?, ?> project, FilePath ws) throws IOException, InterruptedException {
    }

    /**
     * Creates a tar ball.
     */
    public WorkspaceSnapshot snapshot(AbstractBuild<?, ?> build, FilePath ws, TaskListener listener) throws IOException, InterruptedException {
        File wss = new File(build.getRootDir(),"workspace.zip");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(wss));
        try {
            ws.zip(os);
        } finally {
            os.close();
        }
        return new WorkspaceSnapshotImpl();
    }

    public static final class WorkspaceSnapshotImpl extends WorkspaceSnapshot {
        public void restoreTo(AbstractBuild<?,?> owner, FilePath dst, TaskListener listener) throws IOException, InterruptedException {
            File wss = new File(owner.getRootDir(),"workspace.zip");
            new FilePath(wss).unzip(dst);
        }
    }

    @Extension
    public static final class DescriptorImpl extends FileSystemProvisionerDescriptor {
        public boolean discard(FilePath ws, TaskListener listener) throws IOException, InterruptedException {
            // the default provisioner doens't do anything special,
            // so allow other types to manage it
            return false;
        }

        public String getDisplayName() {
            return "Default";
        }
    }
}
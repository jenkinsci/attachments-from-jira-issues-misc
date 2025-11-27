package hudson.bugs;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.*;
import hudson.model.*;
import hudson.slaves.DumbSlave;
import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.HudsonTestCase;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: bshine
 */
public class NodeRenamingTest extends HudsonTestCase {    
    /**
     * Tests renaming a node with tied jobs
     */
    @Bug(4232)
    public void testConfiguration() throws Exception, IOException {
        HudsonTestCase.WebClient client = new HudsonTestCase.WebClient();
        DumbSlave origSlave = createSlave();
        String origName = origSlave.getNodeName();

        // Create a project
        FreeStyleProject proj = createFreeStyleProject("lasagna");

        assertTrue(proj.isBuildable());
        // Verify that the project is not tied to a node
        Label label = proj.getAssignedLabel();
        assertNull("should not be tied to a node now", proj.getAssignedLabel());
        HtmlPage projConfigPage = client.goTo("job/lasagna/configure");
        HtmlForm projConfigForm = projConfigPage.getFormByName("config");
        // give it a description
        HtmlTextArea descriptionTextArea = projConfigForm.getTextAreaByName("description");
        String cannedDescription = "A lovely project with noodles, cheese, and tomato sauce"; 
        descriptionTextArea.setText(cannedDescription);

        // Tie it to the slave we just created node
        HtmlInput slaveAffinityCheckbox = projConfigForm.getInputByName("hasSlaveAffinity");
        Page pageNow = slaveAffinityCheckbox.setChecked(true);

        HtmlSelect slaveSelectElement = projConfigForm.getSelectByName("slave");
        slaveSelectElement.setSelectedAttribute(origName, true);
        submit(projConfigForm); 

        // Verify that it's tied to the original node
        List<Project> projects = hudson.getProjects();
        Project lasagna = projects.get(0); // just one item, it must be lasagna
        assertEquals("found project lasagna", "lasagna", lasagna.getName());
        assertTrue(lasagna.isBuildable());
        assertEquals("the description we set should still be there",
                cannedDescription, lasagna.getDescription());
        
        // Backwards way to figure out if lasagna is tied to a node:
        Label lasagnaLabel = lasagna.getAssignedLabel();
        assertNotNull("now we should be tied to a node", lasagnaLabel);

        // Verify that there are projects tied to the original node
        Computer myOnlySlave = hudson.getComputer(origName);
        List<AbstractProject> tiedJobs = myOnlySlave.getTiedJobs();
        int numTiedJobs = tiedJobs.size();
        assertEquals("should have one tied job", 1, numTiedJobs);

        // Build the project, just to prove we can
        System.out.println("Scheduling first build of lasagna...");
        java.util.concurrent.Future<FreeStyleBuild> theBuild = proj.scheduleBuild2(0); 
        assertBuildStatus(Result.SUCCESS, theBuild.get(1, TimeUnit.MINUTES));

        //
        // Rename the original slave to "call_me_william"
        //
        HtmlPage configPage = client.goTo("computer/" + origName + "/configure");
        HtmlForm form = configPage.getFormByName("config");
        HtmlInput nameInput = form.getInputByName("_.name");
        String william = "call_me_william";
        nameInput.setValueAttribute(william);
        form.submit((HtmlButton)last(form.getHtmlElementsByTagName("button")));

        // Make sure we've still got the right project...
        projects = hudson.getProjects();
        Project lasagnaLater = projects.get(0); // just one item, it must be lasagna
        assertEquals("found project lasagnaLater", "lasagna", lasagna.getName());
        assertEquals("the description we set should still be there",
                        cannedDescription, lasagnaLater.getDescription());        

        // Now schedule a build of the project. I want it to automatically
        // find the renamed node, and build on that; instead, it seems to
        // hang without finding a node that can build it. 
        System.out.println("Scheduling second build of lasagna -- if it fails, it demonstrates bug 4232");
        java.util.concurrent.Future<FreeStyleBuild> secondBuild = lasagnaLater.scheduleBuild2(0);
        assertBuildStatus(Result.SUCCESS, secondBuild.get(1, TimeUnit.MINUTES));
        System.out.println("whew. done");
    }
}

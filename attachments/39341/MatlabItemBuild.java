package matlabjenkins.matlabjnk;

import hudson.init.Initializer;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.Executor;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.annotation.Nonnull;



public class MatlabItemBuild extends Build<MatlabItemProject, MatlabItemBuild> {

	protected MatlabItemBuild(MatlabItemProject project) throws IOException {
		super(project);
		
	}
	
	



	public MatlabItemBuild(MatlabItemProject job, Calendar timestamp) {
		super(job, timestamp);

	}




	public MatlabItemBuild(MatlabItemProject project, File buildDir) throws IOException {
		super(project, buildDir);
	}



	@Override
	public void run() {


		//super.run();

		execute(new RunExecution() {

			
			@Override
			public @Nonnull
			Result run(@Nonnull BuildListener listener) throws Exception,
					hudson.model.Run.RunnerAbortedException {
				
				listener.getLogger().println("Started to run the Build"+this.getBuild().toString());

				Result r = null;

				r = Executor.currentExecutor().abortResult();
								
				return r;
			}

			@Override
			public void post(@Nonnull BuildListener listener) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cleanUp(@Nonnull BuildListener listener)
					throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			



		});
		
		

	}
}

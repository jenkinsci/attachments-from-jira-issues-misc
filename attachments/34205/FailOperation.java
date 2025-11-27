/*
 * The MIT License
 * 
 * Copyright (c) 2014 IKEDA Yasuyuki
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package hudson.plugins.build_timeout.operations;

import static hudson.util.TimeUnit2.MILLISECONDS;
import static hudson.util.TimeUnit2.MINUTES;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Executor;
import hudson.model.Node;
import hudson.model.Result;
import hudson.plugins.build_timeout.BuildTimeOutOperation;
import hudson.plugins.build_timeout.BuildTimeOutOperationDescriptor;
import hudson.remoting.Callable;
import hudson.util.ProcessTree;
import hudson.util.ProcessTree.OSProcess;

/**
 * Fail the build.
 */
public class FailOperation extends BuildTimeOutOperation {
    @DataBoundConstructor
    public FailOperation() {
    }
    
    /**
     * @param build
     * @param listener
     * @param effectiveTimeout
     * @return
     * @see hudson.plugins.build_timeout.BuildTimeOutOperation#perform(hudson.model.AbstractBuild, hudson.model.BuildListener, long)
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, BuildListener listener, long effectiveTimeout) {
        long effectiveTimeoutMinutes = MINUTES.convert(effectiveTimeout,MILLISECONDS);
        // Use messages in hudson.plugins.build_timeout.Messages for historical reason.
        listener.getLogger().println(hudson.plugins.build_timeout.Messages.Timeout_Message(
                effectiveTimeoutMinutes,
                hudson.plugins.build_timeout.Messages.Timeout_Failed())
        );
        Executor e = build.getExecutor();
        if (e != null) {
            Node n = build.getBuiltOn();
            if( n != null && build.getClass().getName().startsWith( "hudson.maven." ) ) {
                listener.getLogger().println( "Build Timeout FailOperation: maven build (" + build.getClass().getName() + ") detected; killing child processes directly..." );
                EnvVars envVars = build.getCharacteristicEnvVars();
                Boolean success = false;
                try {
					success = n.getChannel().call( new MavenProccessKiller( envVars ) );
				} catch( IOException e2 ) {
	                listener.getLogger().println( "Build Timeout FailOperation: MavenProcessKiller got an IOException; falling back to the Jenkins default process killer.  Message: " + e2.getMessage() );
				} catch( InterruptedException e2 ) {
	                listener.getLogger().println( "Build Timeout FailOperation: MavenProcessKiller got an InterruptedException; falling back to the Jenkins default process killer.  Message: " + e2.getMessage() );
				}
                if( success ) {
                	// wait ten seconds to let the effects of our MPK propagate through the system
                	try {
						Thread.sleep( 10 * 1000 );
					} catch( InterruptedException e1 ) {
					}
                }
                // and then run this that kills off everything quite nicely even if our MPK failed;
                // we don't use this to start with because, on its own, it results in maven builds
                // reporting that they were "aborted".
                try {
                	n.createLauncher( listener ).kill( envVars );
                } catch( IOException e3 ) {
	                listener.getLogger().println( "Build Timeout FailOperation: Jenkins default process killer got an IOException; falling back to merely interrupting the build.  Message: " + e3.getMessage() );
	                e.interrupt( Result.FAILURE );
                } catch( InterruptedException e3 ) {
	                listener.getLogger().println( "Build Timeout FailOperation: Jenkins default process killer got an InterruptedException; falling back to merely interrupting the build.  Message: " + e3.getMessage() );
	                e.interrupt( Result.FAILURE );
                }
            } else {
                e.interrupt( Result.FAILURE );
            }
        }
        
        return true;
    }
    
    @Extension(ordinal=50) // should be located at the second.
    public static class DescriptorImpl extends BuildTimeOutOperationDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.FailOperation_DisplayName();
        }
    }
    
    private static class MavenProccessKiller implements Callable<Boolean,InterruptedException> {
    	private Map<String,String> vars;
    	
    	@SuppressWarnings( "unused" ) // Empty constructor for serialization.
		protected MavenProccessKiller() {}

		public MavenProccessKiller( EnvVars envVars ) {
			// put this in a hashmap because I don't know what sort of extra
			// data an EnvVars object might drag along into serialization.
			vars = new HashMap<String,String>( envVars );
		}

		public Boolean call() throws InterruptedException {
			Iterator<OSProcess> i = ProcessTree.get().iterator();
			Map<Integer,OSProcess> procs = new HashMap<Integer,OSProcess>();
			Set<Integer> parents = new HashSet<Integer>();
			while( i.hasNext() ) {
				OSProcess p = i.next();
				if( p.hasMatchingEnvVars( vars ) ) {
					OSProcess parent = p.getParent();
					if( parent != null ) {
						parents.add( parent.getPid() );
					}
					procs.put( p.getPid(), p );
				}
			}
			procs.keySet().removeAll( parents );
			if( procs.isEmpty() ) {
				return false; // this should really never happen, but if it does, give up here.
			}
			// and this then kills just leaf nodes.
			for( OSProcess p : procs.values() ) {
				p.kill();
			}
			return true;
		}
    }
}

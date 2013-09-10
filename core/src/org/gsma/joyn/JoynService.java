/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.gsma.joyn;

import android.content.Context;

/**
 * Abstract joyn service
 * 
 * @author Jean-Marc AUFFRET
 */
public abstract class JoynService {
    /**
     * Information about the current build
     */
    public static class Build {
        /**
         * List of GSMA versions
         */
        public static class GSMA_CODES {
	    	/**
	    	 * GSMA RCS-e hotfixes version
	    	 */
	    	public final static int RCSE_HOTFIXES_1_2 = 0;
	    	
	    	/**
	    	 * GSMA RCS-e Blackbird version
	    	 */
	    	public final static int RCSE_BLACKBIRD = 1;
        }
        
        /**
         * List of version codes
         */
        public static class VERSION_CODES {
        	/**
        	 * The original first version of joyn API
        	 */
        	public final static int BASE = 1;
        }
    	
    	/**
    	 * GSMA version number
    	 * 
    	 * @see Build.GSMA_CODES
    	 */
    	public static final int GSMA_VERSION = GSMA_CODES.RCSE_HOTFIXES_1_2;

    	/**
    	 * API release implementor name
    	 */
    	public static final String API_CODENAME = "GSMA";

    	/**
    	 * API version number
    	 * 
    	 * @see Build.VERSION_CODES
    	 */
    	public static final int API_VERSION = VERSION_CODES.BASE;

    	/**
    	 * Internal number used by the underlying source control to represent this build
    	 */
    	public static final int API_INCREMENTAL = 0;
   	   	    	
    	private Build() {
        }    	
    }
    
    /**
     * Service error
     */
    public static class Error {
    	/**
    	 * Internal error
    	 */
    	public final static int INTERNAL_ERROR = 0;

    	/**
    	 * Service has been disabled
    	 */
    	public final static int SERVICE_DISABLED = 1;
    	    	
    	/**
    	 * Service connection has been lost
    	 */
    	public final static int CONNECTION_LOST = 2;
        
    	private Error() {
        }    	
    }

	/**
	 * Application context
	 */
	protected Context ctx;
	
	/**
	 * Service listener
	 */
	protected JoynServiceListener serviceListener;
	
	/**
	 * Constructor
	 * 
     * @param ctx Application context
     * @param listener Service listener
	 */
	public JoynService(Context ctx, JoynServiceListener listener) {
		this.ctx = ctx;
		this.serviceListener = listener;
	}
	
    /**
     * Connects to the API
     */
    public abstract void connect();
    
    /**
     * Disconnects from the API
     */
    public abstract void disconnect();	

    /**
     * Returns true if the service is connected, else returns false
     * 
	 * @return Returns true if connected else returns false
     */
    public abstract boolean isServiceConnected();
}
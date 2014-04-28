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
package com.gsma.services.rcs;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.IInterface;

/**
 * Abstract joyn service
 * 
 * @author Jean-Marc AUFFRET
 */
public abstract class JoynService {
    /**
     * Action to broadcast when joyn service is up.
     */
    public static final String ACTION_RCS_SERVICE_UP = "com.gsma.services.rcs.action.RCS_SERVICE_UP";

	/**
	 * Information about the current build
	 */
	public static class Build {
		/**
		 * List of version codes
		 */
		public static class VERSION_CODES {
			/**
			 * The original first version of joyn API
			 */
			public final static int BASE = 1;

			/**
			 * Blackbird version of joyn API
			 */
			public final static int BLACKBIRD = 2;
		}

		/**
		 * API release implementor name
		 */
		public static final String API_CODENAME = "GSMA";

		/**
		 * API version number
		 * 
		 * @see Build.VERSION_CODES
		 */
		public static final int API_VERSION = VERSION_CODES.BLACKBIRD;

		/**
		 * Internal number used by the underlying source control to represent
		 * this build
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
	 * API interface
	 */
	private IInterface api = null;

	/**
	 * Service version
	 */
	private Integer version = null;

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
	 * Call specific method on the API interface
	 * 
	 * @param method Method to be called
	 * @param param Parameters of the method
	 * @param paramClass Class of the parameter passed
	 * @return Object
	 * @throws JoynServiceException
	 */
	private Object callApiMethod(String method, Object param, Class paramClass) throws JoynServiceException {
		if (api != null) {
			Class c = api.getClass();
			try {
				if (param != null) {
					Method m = c.getDeclaredMethod(method, paramClass);
					return m.invoke(api, param);
				} else {
					Method m = c.getDeclaredMethod(method, null);
					return m.invoke(api);
				}
			} catch (Exception e) {
				throw new JoynServiceException(e.getMessage());
			}
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}
	
	/**
	 * Set API interface
	 * 
	 * @param api API interface
	 */
	protected void setApi(IInterface api) {
		this.api = api;
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
	public boolean isServiceConnected() {
		return (api != null);
	}

	/**
	 * Returns service version
	 * 
	 * @return Version
	 * @see JoynService.Build.VERSION_CODES
	 * @throws JoynServiceException
	 */
	public int getServiceVersion() throws JoynServiceException {
		if (api != null) {
			if (version == null) {
				try {
					version = (Integer)callApiMethod("getServiceVersion", null, null);
				} catch (Exception e) {
					throw new JoynServiceException(e.getMessage());
				}
			}
			return version;
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}

	/**
	 * Returns true if the service is registered to the platform, else returns
	 * false
	 * 
	 * @return Returns true if registered else returns false
	 * @throws JoynServiceException
	 */
	public boolean isServiceRegistered() throws JoynServiceException {
		if (api != null) {
			return (Boolean)callApiMethod("isServiceRegistered", null, null);
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}

	/**
	 * Registers a listener on service registration events
	 * 
	 * @param listener Service registration listener
     * @throws JoynServiceException
	 */
	public void addServiceRegistrationListener(JoynServiceRegistrationListener listener) throws JoynServiceException {
		if (api != null) {
			callApiMethod("addServiceRegistrationListener", listener, IJoynServiceRegistrationListener.class);
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}
	
	/**
	 * Unregisters a listener on service registration events
	 * 
	 * @param listener Service registration listener
     * @throws JoynServiceException
	 */
	public void removeServiceRegistrationListener(JoynServiceRegistrationListener listener) throws JoynServiceException {
		if (api != null) {
			callApiMethod("removeServiceRegistrationListener", listener, IJoynServiceRegistrationListener.class);
		} else {
			throw new JoynServiceNotAvailableException();
		}
	}
}

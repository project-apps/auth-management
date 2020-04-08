package org.auth.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractGenericController {
	
	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());
}

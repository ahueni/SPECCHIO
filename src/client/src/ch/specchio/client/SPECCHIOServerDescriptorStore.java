package ch.specchio.client;

import java.io.IOException;
import java.util.Iterator;

/**
 * This class represents a store of SPECCHIO connection details.
 */
public abstract class SPECCHIOServerDescriptorStore {
	
	/**
	 * Add the an account to the store.
	 * 
	 * @param d		the descriptor of the server on which the new account exists
	 * 
	 * @throws IOException te configuration data is inaccessible
	 */
	public abstract void addServerDescriptor(SPECCHIOServerDescriptor d) throws IOException;
	
	/**
	 * Get an iterator through all of the descriptors in the store.
	 * 
	 * @return an iterator
	 */
	public abstract Iterator<SPECCHIOServerDescriptor> getIterator();

}

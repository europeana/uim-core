/* Type.java - created on Jul 29, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Jul 29, 2011
 */
public enum AddressType {
    /** primary address */
    PRIMARY,

    /** billing address if different from primary */
    BILLING,

    /** alternative address */
    ALTERNATIVE,
}
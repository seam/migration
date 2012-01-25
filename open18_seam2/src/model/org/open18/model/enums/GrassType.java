package org.open18.model.enums;

/**
 * Types of grass used on the putting, tee boxes or fairways. According to
 * putting-greens.com, there are only two types of grass used for putting
 * greens, creeping bent or bermuda. Bent grass is the dominate type.
 * 
 * @see http://www.putting-greens.com/
 */
public enum GrassType {
    BENT, BERMUDA, ZOYSIA, BLUEGRASS, RYE, FESCUE, UNKNOWN;

    public boolean isValidForGreens() {
        return this.equals( BENT ) || this.equals( BERMUDA ) || this.equals( UNKNOWN );
    }

    public boolean isValidForFairways() {
        return true;
    }
}

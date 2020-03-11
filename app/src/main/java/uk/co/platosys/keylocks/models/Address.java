package uk.co.platosys.keylocks.models;

import java.util.List;

/**This interface models a person's address (it maps to Android contacts in some way which we will work
 * out as we build it)
 *
 *
 *
 * In KeyLocks, an address is a set of Identities. Identities can be attached to Locks.
 *
 */
public interface Address {
    public List<Identity> getIdentities();
}

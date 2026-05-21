package io.siuolplex.fhcoal.compat;

// Ok so this is NOT implemented for things you can get away with not registering (Create Item Attribute Types)
// Only use it for blocks or items that can be hidden.
public interface ModDependent {
    boolean parentModsAvailable();
}

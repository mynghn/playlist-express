package mynghn.common.credential;

public interface CredentialManager<C> {

    /**
     * Return API credentials by any means <br/>
     * e.g. lazy load, read from file, etc...
     * @return Parametrized type API credentials of some 3rd party service
     */
    C getCredentials();
}

# TOOP Connector NG

The TOOP Connector NG is a set of shared utility functions that you CAN include in your DC (Data Consumer) and/or DP (Data Provider) to perform common tasks that are required for a safe and interoperable data exchange. The TOOP Connector NG can be used both as a Java library AND via an REST API.  
The TOOP Connector NG is a Java only solution. Other environments like .NET etc. are currently not supported.

It uses the shared components from:
* https://github.com/TOOP4EU/toop-commons-ng
* https://github.com/TOOP4EU/data-services-directory

#  License

All rights to the results that are made available via this repository are owned by their respective creators, as identified in the relevant file names. Unless explicitly indicated otherwise, the results are made available to you under the EUPL, Version 1.2, an EU approved open source licence. For a full version of the licence and guidance, please visit https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12

Note that the results are protected by copyright, and all rights which are not expressly licenced to you by the owners or granted by applicable law are explicitly reserved.

This repository is the only formal source of the results of the TOOP project, an action that was funded by the EU Horizon 2020 research and innovation programme under grant agreement No 737460 (see https://toop.eu/). If you have obtained the results elsewhere or under a different licence, it is likely that this is in violation of copyright law. In case of doubt, please contact us.  

# Modules

This project consists of the following sub modules:
* **tc-api** - the shared data structures, building on top of the "toop-edm" project (see toop-commons-ng)
* **tc-mem-external** - connection between TOOP Connector and an external AS4 gateway (mem = message exchange module)
* **tc-mem-phase4** - a built-in AS4 gateway for the TOOP Connector avoiding the use of an external AS4 gateway
* **tc-main** - contains the business logic functionality as an embeddable JAR
* **tc-web-api** - contains the REST API that invokes the functionality from "tc-main"
* **tc-webapp** - a web application (WAR) that includes the "tc-web-api" project
* **tc-jetty** - a self-contained application that combines the "tc-webapp" project with a Jetty instance - this serves as the basis for the Docker image but can also be run standalone

# Configuration

The configuration file is called `application.properties` and is search by default in the classpath.
Custom configuration files can be located like this:
1. if the system property `config.resource` or the environment variable `CONFIG_RESOURCE` is present, and it points to an existing classpath resource, the first one matching is used - priority 200 or determined by the system property `config.resource.priority` or the environment variable `CONFIG_RESOURCE_PRIORITY`. Note: the file type is determined by the extension and defaults to "properties".
1. if the system property `config.file` or the environment variable `CONFIG_FILE` is present, and it points to an existing file, it is used - priority 200 or determined by the system property `config.file.priority` or the environment variable `CONFIG_FILE_PRIORITY`. Note: the file type is determined by the extension and defaults to "properties".
1. if the system property `config.url` or the environment variable `CONFIG_URL` is present, and it points to an existing URL, it is used - priority 200 or determined by the system property `config.url.priority` or the environment variable `CONFIG_URL_PRIORITY`. Note: the file type is determined by the extension and defaults to "properties".

The following configuration properties are **supported** - some of them have default values:

* **`global.debug`** (boolean) - enable development debug functionality
* **`global.production`** (boolean) - enable production mode (performance optimizations, less checks)
* **`global.instancename`** (string) - this is only used as the log prefix if the tracker is used
* **`toop.tracker.enabled`** (boolean) - enable or disable the remote tracker
* **`toop.tracker.url`** (string) - the URL where the tracker is collecting data elements
* **`toop.tracker.topic`** (string) - the TOOP tracker topic (left pane)
* **`toop.dsd.service.baseurl`** (string) - the URL of the DSD
* **`toop.r2d2.usedns`** (boolean) - use the SML system to dynamically discover partner systems? This should only be `false` for testing purposes. In production this should always be `true`.
* **`toop.r2d2.sml.name`** (string) - internal name of the SML (only if `toop.r2d2.usedns` is `true`)
* **`toop.r2d2.sml.dnszone`** (string) - the DNS zone of the SML (only if `toop.r2d2.usedns` is `true`)
* **`toop.r2d2.sml.serviceurl`** (string) - the management service URL of the SML (only if `toop.r2d2.usedns` is `true`)
* **`toop.r2d2.sml.clientcert`** (boolean) - is a client certificate need when talking to this SML (only if `toop.r2d2.usedns` is `true`)
* **`toop.r2d2.smp.url`** (string) - the absolute URL of the SMP to use  (only if `toop.r2d2.usedns` is `false`)
* **`toop.r2d2.static.endpointurl`** (string) (since 2.1.0) - the absolute URL of the AS4 endpoint URL. **Caution:** if this is used, dynamic discovery id disabled and all messages are send to the same AS4 gateway
* **`toop.r2d2.static.certificate`** (string) (since 2.1.0) - the PEM encoded X509 certificate of the AS4 gateway. **Caution:** if this is used, dynamic discovery id disabled and all messages are send to the same AS4 gateway
* **`toop.mem.implementation`** (string) - the ID of the AS4 implementation to use. Can be either `external` or `phase4` - depending on this, different configuration properties must be configured (see below)
* **`http.proxy.enabled`** (boolean) - is an HTTP proxy needed?
* **`http.proxy.address`** (string) - the URL of the proxy server (including the scheme) (e.g. `http://proxy.local`)
* **`http.proxy.port`** (int) - the port to access the HTTP proxy server (e.g. `8080`)
* **`http.proxy.non-proxy`** (string) - a list of hosts that should not be proxied. Use the pipe character as the separator for multiple entries (as in `localhost|127.0.0.1`).
* **`http.tls.trustall`** (boolean) - use this to disable the hostname and trusted certificate check for SSL/TLS connections
* **`http.connection-timeout`** (int) - the HTTP connection timeout in milliseconds
* **`http.read-timeout`** (int) - the HTTP read/socket timeout in milliseconds
* **`toop.webapp.data.path`** (string) (since 2.0.0-rc4) - the absolute path, where data should be stored.
* **`toop.mem.incoming.url`** (string) - the URL inside your DC/DP where incoming messages (of type `TCIncomingMessage`) should be send to (for receiving incoming message). Note: this property is only needed, if HTTP interface between DC/DP and TC is used.

Additionally the configuration items of the SMP client should be configured. The complete description can be found at https://github.com/phax/peppol-commons#configuration - the main items are:
* **`truststore.type`** (string): the type of key store to be used. Possible values are `JKS` and `PKCS12`. Defaults to `JKS` (which was the implicit default prior to v6).
* **`truststore.path`** (string): the location of the trust store (of the specified type) to be used.
* **`truststore.password`** (string): the password to access the trust store.

Note: see https://github.com/TOOP4EU/toop-connector-ng/blob/master/tc-webapp/src/main/resources/application.properties for the default configuration file

### Properties for MEM implementation `external`

* **`toop.mem.as4.endpoint`** (string) - the URL where the external AS4 gateway (Holodeck) listens to messages to be send out
* **`toop.mem.as4.gw.partyid`** (string) - the AS4 gateway party ID
* **`toop.mem.as4.tc.partyid`** (string) - the AS4 TOOP Connector party ID
* **`toop.mem.as4.to-party-id-type`** (string) (since 2.0.2) - the AS4 `To/PartyId/@type` value. This is optional and defaults to `null`. This is for Domibus compatibility only.
* **`toop.mem.as4.notificationWaitTimeout`** (long) - the timeout for a notification in milliseconds
* **`toop.mem.outgoing.dump.enabled`** (boolean) (since v2.0.0-rc3) - enable or disable the dumping of outgoing messages. By default this is disabled. 
* **`toop.mem.outgoing.dump.path`** (string) (since v2.0.0-rc3) - the file system directory in which the dumps of the outgoing messages are stored. This should be an absolute path. The filenames in the directory start with `toop-mem-external-outgoing-`.
* **`toop.mem.incoming.dump.enabled`** (boolean) (since v2.0.0-rc3) - enable or disable the dumping of incoming messages. By default this is disabled.
* **`toop.mem.incoming.dump.path`** (string) (since v2.0.0-rc3) - the file system directory in which the dumps of the incoming messages are stored. This should be an absolute path. The filenames in the directory start with `toop-mem-external-incoming-`.

Note: the receiving endpoint for MEM implementation `external` is `/from-as4` - this must be part of your SMP endpoint URL.

### Properties for MEM implementation `phase4`

* **`phase4.datapath`** (string) - the absolute path to a local directory to store data
* **`phase4.debug.http`** (boolean) - enable or disable HTTP debugging for AS4 transmissions. The default value is `false`.
* **`phase4.debug.incoming`** (boolean) - enable or disable debug logging for incoming AS4 transmissions. The default value is `false`.
* **`phase4.dump.incoming.path`** (string) (since v2.0.0-rc3) - an optional absolute directory path where the incoming AS4 messages should be dumped to. Disabled by default.
* **`phase4.dump.outgoing.path`** (string) (since v2.0.0-rc3) - an optional absolute directory path where the outgoing AS4 messages should be dumped to. Disabled by default.
* **`phase4.send.fromparty.id`** (string) (since v2.0.0-rc3) - the from party ID to be used for outgoing messages. Previous versions need to use **`toop.mem.as4.tc.partyid`** - starting from RC3 this property is still used as a fallback)
* **`phase4.send.fromparty.id.type`** (string) (since 2.0.2) - the AS4 `From/PartyId/@type` value. This is optional and defaults to `null`. This is for Domibus compatibility only.
* **`phase4.send.toparty.id.type`** (string) (since 2.0.2) - the AS4 `To/PartyId/@type` value. This is optional and defaults to `null`. This is for Domibus compatibility only.
* **`phase4.send.response.folder`** (string) - an optional folder, where sent responses should be stored. If this property is not provided, they are not stored
* **`phase4.keystore.type`** (string) - the type of the keystore (either "JKS" or "PKCS12" - case insensitive) - defaults to JKS.
* **`phase4.keystore.path`** (string) - the path to the keystore (can be classpath relative or an absolute file)
* **`phase4.keystore.password`** (string) - the password to access the keystore 
* **`phase4.keystore.key-alias`** (string) - the alias of the key in the keystore (may be case sensitive)
* **`phase4.keystore.key-password`** (string) - the password to access the key in the keystore
* **`phase4.truststore.type`** (string) - the type of the truststore (either "JKS" or "PKCS12" - case insensitive) - defaults to JKS.
* **`phase4.truststore.path`** (string) - the path to the truststore (can be classpath relative or an absolute file)
* **`phase4.truststore.password`** (string) - the password to access the truststore 

Note: the receiving endpoint for MEM implementation `phase4` is `/phase4` - this must be part of your SMP endpoint URL.

# Running

The `tc-webapp` module is a web application that can be deployed in arbitrary application servers supporting the Servlet specification 3.1 and onwards.

**Note** there were classloader issues running TOOP Connector NG in Tomcat (prior to - we suggest to use Jetty until we have figured out how to resolve it. 

## tc-jetty

Standalone version of TOOP Connector NG

Usage:

```
Usage: tc-jetty [-hV] [-p Port] [-s Stop Port] command
Standalone TOOP Connector NG
      command                What to do. Options are start, stop
  -h, --help                 Show this help message and exit.
  -p, --port Port            Port to run the TOOP Connector on (default: 8080)
  -s, --stopPort Stop Port   Internal port to watch for the shutdown command
                               (default: 8079)
  -V, --version              Print version information and exit.
```

Start via `start` and shut it down with `stop`.

How to run it: invoke the following command, replacing `x.y.z` with the effective version number

```
java -jar tc-jetty-x.y.z-full.jar start
```

to add a custom configuration file add the property "config.file" like this:

```
java -Dconfig.file=/path/to/your/file.properties -jar tc-jetty-x.y.z-full.jar start
```

# Building

Requires at least

* Java 1.8 or later
* Apache Maven for building

Do a simple `mvn clean install` on the command line.

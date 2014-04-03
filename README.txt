INDEX
=====

1/ Project structure
2/ Installation
3/ Test import from file
4/ GWT Development mode
5/ Configuration
6/ Technologies in use 


#1 PROJECT STRUCTURE
====================

Path                              Name                                                    Description
-------------------------------------------------------------------------------------------------------------------------------------------
/                                 Unified Ingestion Manager                                The root maven project

/framework                        Unified Ingestion Manager: CORE Framework                This sub modules contains all core modules of the UIM
/framework/common                 Unified Ingestion Manager: Common                        Shared classes and resources (e.g. for testing)
/framework/api                    Unified Ingestion Manager: API                           The UIM API bundle, used by plugins
/framework/orchestration/basic    Unified Ingestion Manager: API Standard Orchestrator     The basic/default implementation of the API
/framework/storage/memory         Unified Ingestion Manager: Storage Backend Memory        In-memory implementation of the storage engine
/framework/logging/memory         Unified Ingestion Manager: API Memory Only Logging       In-memory implementation of the logging engine
/framework/integration            Unified Ingestion Manager: Integration tests             The integration tests, using PAX-Exam
/framework/gui/uim-gui-basic      Unified Ingestion Manager: Common GWT UIM Modules        Common UIM GWT based widgets for reuse
/framework/gui/uim-gui-basic      Unified Ingestion Manager: Basic UIM Control Panel GUI   Basic UIM GWT frontend
/framework/etc                    Unified Ingestion Manager: UIM Karaf Features            Specifies default bundles to be installed in Karaf

/logging/database/model           Unified Ingestion Manager: Database Logging Model        Definition of the model for database based logging
/logging/database/engine          Unified Ingestion Manager: Database Logging Engine       Implementation of the logging against a database (Hibernate)
/storage/mongo                    Unified Ingestion Manager: Storage Backend Mongo         Mongodb implementation of the storage engine

/plugins/check                    Unified Ingestion Manager: TEL Check Plugins             Module contains plugins for UIM that provides some kind of data validation
/plugins/check/edm                Unified Ingestion Manager: TEL EDM Check Plugin          This module performs validation checks on EDM using the TEL data model as source model	
/plugins/check/field              Unified Ingestion Manager: TEL Field Check Plugin        This module provides statistics on which fields have been field (e.g. how many records in a collection contains titles) 
/plugins/check/link               Unified Ingestion Manager: TEL Link Check Plugin         This module validates links for a sample of records in a collection
/plugins/check/link-collection    Unified Ingestion Manager: TEL Link Collection Check     This module does link validation for whole collections
/plugins/solr                     Unified Ingestion Manager: SOLR Base Plugins             Base module for all osgi plugins that expose SOLR in different version
/plugins/solr14                   Unified Ingestion Manager: SOLR 1.4 Plugin               OSGi module to expose SOLR 1.4
/plugins/solr3                    Unified Ingestion Manager: SOLR 3.x Plugins              OSGi module to expose SOLR 3.x        
/plugins/solr4                    Unified Ingestion Manager: SOLR 4.x Plugins              OSGi module to expose SOLR 4.x

/external/repox                   Unified Ingestion Manager: External Service Repox        This is a connection from UIM to Repox (harvesting tool)
/external/sugar                   Unified Ingestion Manager: External Service SugarCRM     This is a connection from UIM to SugarCRM (customer relationship tool)

/model                            Unified Ingestion Manager: Models                        This is the base module for model definitions
/model/adapters                   Unified Ingestion Manager: Model Adapters                Module for implementations of adapters to make interoperability between Europeana and TEL possible
/model/common                     Unified Ingestion Manager: Model Common                  POJOs definition that are considered more common and are shared between Europeana and TEL model
/model/edm                        Unified Ingestion Manager: Model EDM                     Model specific to EDM
/model/europeana                  Unified Ingestion Manager: Model Europeana               Model specific to Europeana
/model/tel                        Unified Ingestion Manager: Model TEL                     Model specific to The European Library

/versions                         Unified Ingestion Manager: Versions                      This one contains previous released versions

#2 INSTALLATION
===============

1) Get Apache Felix Karaf at http://karaf.apache.org/
   (Currently Karaf works only with version 2.2.9 and above)   

2) Build UIM with maven
   - 'mvn install'  (should now succeed)

3) Start Karaf:
   - go to the Karaf main directory
   - start it 'bin/start'

4) Connect to Karaf:
   - go to the Karaf main directory
   - connect with 'bin/client'

5) Set-up dependencies in Karaf:
   - install necessary features:
   - features:install spring

6) Configure UIM Feature
   - 'features:addurl file://<project-path>/etc/uim-features.xml'
   - you can check if the feature "uim-core" is available via 'features:list'
   - 'features:install uim-core'

7) Configure UIM Frontend Features
     - features:install war
     - features:install webconsole
     - features:install uim-europeana-gui
     
8) a.) Configure UIM Persistned Backend Features (Hibernate based logging and MongoDB based storage engine)
     - features:install uim-store-pers
     
   b.) Configure UIM In-Memory Backend Features
     - features:install uim-store-mem
 
9) Verify if UIM is up and running (Note that auto completion with TAB does only work when blueprint is used)
   - in Karaf shell: 'uim:info'

8) Load/Show sample data:
   - in Karaf shell: 'uim:store -o loadSampleData'
   - in Karaf shell: 'uim:store -o listProvider'
   - in Karaf shell: 'uim:store -o listCollection'

9) Check the web GUI
   Fire up a browser at http://127.0.0.1:8181/gui

#3 TEST IMPORT FROM FILE
========================

1) Install karaf and the UIM API

2) Build UIM module import file (build automatically when building the toplevel of UIM)

3) Install import file: 'osgi:install -s mvn:eu.europeana/europeana-uim-import-file/1.0.0-SNAPSHOT'

4) Verify if UIM File Import is up and running:
   - in Karaf shell: 'uim:file'
   - should complain about missing arguments
   
5) Import ESE file:
   - in Karaf shell: 'uim:file -c 1 <project-path>/common/src/test/resources/readingeurope.xml'
                     'uim:file -c 2 <project-path>/common/src/test/resources/readingeurope.xml'

#4 GWT DEVELOPMENT MODE
=======================

We use GWT (Google Web Toolkit) for frontend development.

In order not to wait too long for compilation, only Google Chrome compliant javascript is currently compiled.
    Change this in the application.gwt.xml if you need to use another browser

In order to run the development mode:

1) make sure Karaf is up and running and UIM is active
2) launch the GWT development mode:
   - 'cd gui/uim-gui-gwt'
   - mvn gwt:run
3) start a browser at the indicated URL. You may need to install the GWT plugin for frontend development

When developing and changing the GWT server-side, you need to reinstall the WAR on Karaf after having re-installed it to maven via

osgi:update <bundleId>

#5 CONFIGURATION
================

=== Dynamic configuration with Karaf

Use Karaf's 'config' commands to update properties, e.g.

  config:edit eu.europeana.uim
  config:propset defaultStorageEngine MongoStorageEngine
  config:update

This will update the properties and save them on the file system.
Default values are configured via blueprint in the XML definition file.

=== Storage Engine configuration

You can list the avaialble storage engines with

  'uimconfig:storage'

and switch with e.g.

  'uimconfig:storage MongoStorageEngine'

#6 TECHNOLOGIES IN USE
======================

- the project runs on Apache Karaf which bundles Felix and other Apache OSGi projects (http://karaf.apache.org)
- we use the OSGi Blueprint Container specification.
  Karaf/Felix uses the Apache Aries implementation for that purpose, which handles inversion of control through declarative configuration (see the OSGI-INF.blueprint packages)
- we use PAX Exam for integration tests (http://wiki.ops4j.org/display/paxexam/Pax+Exam)
- we further use Spring for dependency injection in JUnit tests (not integration tests, nor runtime) (http://www.springsource.org/)

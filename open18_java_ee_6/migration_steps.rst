################################################################################
Seam in Action Open18 to Java EE 6 Migration
################################################################################

This tutorial will walk through converting the Open18 application from Dan 
Allen's *Seam in Action* from Seam 2 to Java EE 6. It will be as plain Java EE 6
as possible. However, there are two large gaps in this migration which will
require projects outside the core of Java EE 6: CRUD scaffolding and application
security. For those two sections CDI Query and Apache Shiro will be used. The
application will also be upgraded to RichFaces 4.1 (the current release at time 
of writing).

********************************************************************************
Migrate to Maven
********************************************************************************

Seam 2, prior to 2.3, used Apache Ant as it's build tool of choice. In migrating 
to vanilla Java EE 6 or when using CDI extensions written by others, Apache 
Maven (or similar such as Gradle, Builder, Ant+Ivy, SBT, etc) will be the
preferred build tool. 

Setup Maven Structure
================================================================================

Apache Maven follows a standard project layout defined below::

  ├── pom.xml
  └── src
      ├── main
      │   ├── java
      │   ├── resources
      │   │   └── META-INF
      │   └── webapp
      │       └── WEB-INF
      └── test
          ├── java
          └── resources

A full explanation of Apache Maven is beyond this tutorial. More information
about Apache Maven (probably more than you'd ever want to know) is available 
from Sonatype at `Maven: The Complete Reference <http://www.sonatype.com/books/mvnref-book/reference/>`_.

The pom.xml file defines all of the dependencies of the project and instructions
for building the project. The directory structure is fairly self explanatory. 
Application source lives under src/main/java. All of the tests are under 
src/test/java. Additional resources for the application such as persistence.xml
will be at src/main/resources/META-INF, and lastly, JSF views and other web
assets such as images, css, javascript, etc. will be under src/main/webapp.

Flesh out pom.xml
================================================================================

Below is the contents for the pom.xml file. There's very little there in terms
of dependencies because the bulk of the dependencies will be provided for the
project by the application server. It is important to note that any dependency
that must be present for either compilation or runtime but is already within the
application server be stated in the pom and given the "provided" scope. Further
information about the sections of the pom.xml file are given as comments in the
actual file::

  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>

      <groupId>org.open18</groupId>
      <artifactId>open18</artifactId>
      <packaging>war</packaging>
      <version>2.0.0.Beta-SNAPSHOT</version>
      <name>Open18 application from Seam in Action</name>

      <properties>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <jboss.javaee6.version>2.1.0.Beta1</jboss.javaee6.version>
          <richfaces.version>4.1.0.Final</richfaces.version>
          <apache.shiro.version>1.2.0</apache.shiro.version>
          <cdi.query.version>1.0.0.Alpha3</cdi.query.version>
          <arquillian.version>1.0.0.CR7</arquillian.version>
          <junit.version>4.10</junit.version>
      </properties>

      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>org.jboss.spec</groupId>
                  <artifactId>jboss-javaee6-specs-bom</artifactId>
                  <version>${jboss.javaee6.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
              <dependency>
                  <groupId>org.richfaces</groupId>
                  <artifactId>richfaces-bom</artifactId>
                  <version>${richfaces.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
              <dependency>
                  <groupId>org.jboss.arquillian</groupId>
                  <artifactId>arquillian-bom</artifactId>
                  <version>1.0.0.CR7</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>

      <dependencies>
          <dependency>
              <groupId>javax.enterprise</groupId>
              <artifactId>cdi-api</artifactId>
              <scope>provided</scope>
          </dependency>
          <dependency>
              <groupId>org.jboss.spec.javax.faces</groupId>
              <artifactId>jboss-jsf-api_2.1_spec</artifactId>
              <scope>provided</scope>
          </dependency>
          <dependency>
              <groupId>org.jboss.spec.javax.el</groupId>
              <artifactId>jboss-el-api_2.2_spec</artifactId>
              <scope>provided</scope>
          </dependency>
          <dependency>
              <groupId>javax.validation</groupId>
              <artifactId>validation-api</artifactId>
              <scope>provided</scope>
          </dependency>
          <dependency>
              <groupId>org.hibernate.javax.persistence</groupId>
              <artifactId>hibernate-jpa-2.0-api</artifactId>
              <scope>provided</scope>
          </dependency>
          <dependency>
              <groupId>org.jboss.spec.javax.ejb</groupId>
              <artifactId>jboss-ejb-api_3.1_spec</artifactId>
              <scope>provided</scope>
          </dependency>
          <dependency>
              <groupId>org.apache.shiro</groupId>
              <artifactId>shiro-core</artifactId>
              <version>${apache.shiro.version}</version>
              <scope>compile</scope>
          </dependency>
          <dependency>
              <groupId>com.ctp.cdi.query</groupId>
              <artifactId>cdi-query-api</artifactId>
              <version>${cdi.query.version}</version>
              <scope>compile</scope>
          </dependency>
          <dependency>
              <groupId>com.ctp.cdi.query</groupId>
              <artifactId>cdi-query-impl</artifactId>
              <version>${cdi.query.version}</version>
              <scope>runtime</scope>
          </dependency>
          <dependency>
              <groupId>org.apache.shiro</groupId>
              <artifactId>shiro-web</artifactId>
              <version>${apache.shiro.version}</version>
              <scope>runtime</scope>
          </dependency>
          <dependency>
              <groupId>org.richfaces.ui</groupId>
              <artifactId>richfaces-components-ui</artifactId>
              <scope>runtime</scope>
          </dependency>
          <dependency>
              <groupId>org.richfaces.core</groupId>
              <artifactId>richfaces-core-impl</artifactId>
              <scope>runtime</scope>
          </dependency>
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>${junit.version}</version>
              <scope>test</scope>
          </dependency>
          <dependency>
              <groupId>org.jboss.arquillian.junit</groupId>
              <artifactId>arquillian-junit-container</artifactId>
              <scope>test</scope>
          </dependency>
      </dependencies>

      <build>
          <finalName>${project.artifactId}</finalName>
          <plugins>
              <plugin>
                  <artifactId>maven-compiler-plugin</artifactId>
                  <version>2.3.2</version>
                  <configuration>
                      <source>1.6</source>
                      <target>1.6</target>
                  </configuration>
              </plugin>
          </plugins>
      </build>

  </project>


********************************************************************************
Migrate to JPA 2.0
********************************************************************************

JSR 317, the update to the Java Persistence API includes a number of updates,
many of which users had been asking for including improved mappings, a criteria
API, ordering of collections, eviction control, access to a second level cache,
and locking improvements. Setup and configuration is the same as the initial JPA
specification, as is usage.

Update persistence.xml to 2.0
================================================================================

JPA 2 is backwards compatible with JPA 1. All entities should work correctly as
they did using a JPA 1 implementation. The version in persistence.xml should be
updated to take advantage of new features though. Such features include the type
safe criteria api, new mappings, and additional methods.

Metamodel Generation
================================================================================

To take full advantage of type saftey, static meta model classes should be
created or generated. The simplest way of doing this is using an annotation
processor such as Hibernate's JPA 2 Metamodel Generator. Additional information
on using this annotation processor can be found in `the documentation <http://docs.jboss.org/hibernate/jpamodelgen/1.1/reference/en-
US/html_single/>`_.

.. todo: should I actually go through the steps?

For this migration, the annotation processor was used once and then removed from
the pom.xml file.

Migrate to Bean Validation
================================================================================

.. todo: Length(max) -> Size(max), NotNull is a package change

.. todo: Remove Seam annotations and create producers for them (golfer[session], round[also has a restrict on it, look into this a bit more based on what Dan said])

.. todo: GolferValidator should be a new JSR303 Validator

********************************************************************************
Migrate to CDI
********************************************************************************

Java EE 6 had a few new additions to the platform, two of them combining to
formally standardize dependency injection for the Enterprise Edition of Java.
These two JSRs are JSR 330, which defines the annotations used for injection,
and JSR 299 which defines how dependency resolution and injection works, scopes
for the platform similar to what Seam 2 provided, and possibly the most
important of all: extensibility for the platform. These two specifications were
developed with input from authors of other dependency injection solutions in
Java such as Spring, Guice, and Seam

With these specifications at least two features of Seam 2 had become part of the
platform. Also many of the features Seam 2 had for working JSF also became part
of the JSF specification. Migration from Seam 2 to Java EE 6 makes sense, and
isn't terribly difficult (of course this depends on some of the features that
were used from Seam 2).  

Activation
================================================================================

Seam 2 required the use of the seam.properties file to mark a jar, or WEB-
INF/classes as containing Seam 2 components. This was mainly an optimization
for scanning purposes. CDI has a similar requirement. Each Bean Archive (jar,
war, etc. containing CDI beans) must contain a META-INF/beans.xml for a jar and
WEB-INF/beans.xml for a war. Some configuration may occur in this file, but
often times it can be left blank. In this migration of Open18 the following
beans.xml is used (src/main/webapp/WEB-INF/beans.xml)::

  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
                http://java.sun.com/xml/ns/javaee
                http://docs.jboss.org/cdi/beans_1_0.xsd">
  </beans>


Substitute Seam 2 annotations for CDI equivalents
================================================================================

Nearly all of the annotations that were Seam 2 based have equivalents in Java EE
6, however, some of them do not or are no longer needed.

Injecting resources
--------------------------------------------------------------------------------

Because Seam components were typically not managed by the container
(unless an EJB happened to be a Seam component, such as a SFSB or SLSB)
all injection has handled used Seam's ``@In``. As mentioned earlier, Java
EE 6 has standardized Dependency Injection using JSR 330. The annotation
now is ``@javax.inject.Inject``. All of the ``@In`` annotations will need
to be replaced.

There is also a difference in defining what is a bean (or a component in Seam
2). In Seam 2 all components needed to be annotated with the ``@Name``
annotation. This is no longer the case. Each class (there are some exceptions,
please refer to JSR 299 or a CDI implementation documentation) with a no-args
constructor is now a managed bean (not to be confused with the JSF Managed
Bean). There is, however the ``@javax.inject.Named`` annotation. It's main
purpose is to register an EL name for the bean. If the bean is not going to be
used in an EL expression, it is not needed.

Producing resources
--------------------------------------------------------------------------------

Seam 2 had a feature called factories which allowed a resource to be created and
outjected. It allowed for a more custom creation than what Seam could do by
calling the no-args constructor. CDI a similar feature called producers. There
are two big differences between factories and producers

* Factories are called each time a resource is requested. Producers can
  be scoped.
* Factories do not support injection. With a producer, each parameter is an
  injected resource.

Because of the first difference, it, at times can be necessary to to create a
wrapper around the actual object desired and modify the information as needed.
For example, the list of new golfers in the Open18 application could be produced
and scoped as a ``@SessionScoped`` resource, but it would never change for that
session. If the list were wrapped within another object, the internal list could
be modified if a new golfer registered during the session and the existing
session could then see the new golfer in the list.

In Open18, besides the example mentioned, another resource which must be
produced which Seam 2 had readily available out of the box is the collection of
messages. This is really a simple ResourceBundle, but it isn't available out of
the box. This allows for a combination of messages similar to what Seam 2
offered, though done in Java code instead of components.xml.

Scopes
--------------------------------------------------------------------------------

Scopes are nothing new when coming from Seam 2. The standard scopes still exist
when using CDI:

* ``@ApplicationScoped``
* ``@SessionScoped``
* ``@ConversationScoped``
* ``@RequestScoped``

There is no business process scope or method scope however. CDI has one
other scope which does not exist in Seam 2: ``@DependentScoped``. This scope
is similar in life as a typical Java object creation. It will last as long
as the containing object survives. There's also one important difference,
when injected, the inject object is the actual object not a proxy like the
other scopes. This scope is also the default scope if no scope is specified
for the bean.

If the need arises for additional scopes, such as a business process scope, CDI
allows for additional scopes to be created. Please refer to the JSR 299 spec or
CDI implementation documentation for defining scopes.

Migrate Query  / Home objects to CDI Query
================================================================================

The application framework within Seam 2 consisting of Home and Query objects has
proved to be very powerful for CRUD based sites. When coupled with seam-gen, it
rivals that of other frameworks such as Grails, Ruby on Rails and the like.
There were some glaring holes with it though. Using inheritance instead of
composition, lack of being able to search for null fields, inability to perform
joins, etc. Java EE 6 doesn't have anything ready to use to fill this gap.
However, what it does have is the ability to extend the platform with portable
CDI extensions! This migration turns to a portable extension called CDI Query to
fill the gap.

CDI works similar to the Home and Query classes of Seam 2 and DAO classes of the
past, and quite neatly couples some of the features of JPA 2 with it. Creating a
DAO, as per `the documentation <http://ctpconsulting.github.com/query>`_ is very
easy. It involves an annotation and extending an interface. Additional queries
can be added as well if needed. It's use of the Static Metamodel objects from
JPA 2 make an excellent replacement for the Query object. CDI Query also
supports using named queries, native queries and simple auditing. All wonder
features out of the box.

To fill the Home object from Seam 2, simple backing beans which manage an
instance of the entity work nicely, and little code is needed to create a
full replacement, when using a CDI Query DAO to perform all the needed
functions. For this migration each entity has a CDI Query DAO created, and
also a backing bean for each entity to act as the buffer between the view and
the backend. These backing beans also happen to be Stateful Session beans in
this instance. It's not required, but the advantages of SFSBs have been
enumerated many times throughout the years. These backing beans are annotated
with one of the scope annotations mentioned earlier and also with ``@Named``
so they can be used in EL.

.. sidebar:: WARNING

  It is best not to directly use JPA entities created by CDI, unless
  they are created by a producer. If CDI manages the life cycle of an entity, JPA
  functionality is lost and the entire object will have to be cloned into a new
  object to be persisted.

.. todo: Many have restrictions, will have to see how to recreate this.

.. todo: method mapping
  createInstance =>
  isWired =>
  getDefinedInstance =>
  persist => save / saveAndFlush
  remove => remove
  update => refresh

.. todo: Trying to use abstract classes to simplify the searching and make it 
  similar to what was done in Seam 2

.. todo: create something to replace roundList and roundCriteria

Changes in the conversation model
================================================================================

CDI has a conversation state similar to Seam 2, however, there are some major
differences. The largest being that only one conversation can be active at a
time per session. This means no nested conversations or multiple conversations
via different browser tabs. The conversation, until CDI 1.1, is also tied
directly to JSF and cannot be used outside of JSF and still remain portable.
There is also no annotation control over the conversation. Instead the
conversation must be injected and then managed (started, ended, timeout
configured, etc.).

.. todo: let them know that there are no more nested conversations, no workspace, etc

********************************************************************************
Migrate to  JSF 2.0
********************************************************************************

.. todo: Also will need something to replace CourseComparison
  ProfileAction needs a replacement
  MultiRoundAction needs a Java replacement
  RegisterAction needs a replacement, may be part of switching to Shiro

.. todo: add h:head and h:body

Update faces-config.xml to 2.0
================================================================================

.. todo: take out the view handler

Migrate to RichFaces 4.1
================================================================================

.. todo: point https://community.jboss.org/wiki/RichFacesMigrationGuide33x-4xMigration for migration

.. todo: a:loadStyle => h:outputStylesheet

Rework Navigation from pages.xml
================================================================================

.. todo: also actions and params

Seam Tags and equivalents in JSF and RichFaces
================================================================================

.. todo: remove the seam namespace

.. todo: s:div, s:fragment, s:link, s:button, s:decorate, s:label, s:span, s:message, s:validateAll, s:convertDateTime, s:convertEnum, s:enumItem, s:selectItems, s:defaultAction 

.. todo: s:link remove propegation and change view to outcome

********************************************************************************
Migrate to Apache Shiro for Security
********************************************************************************

.. todo: AuthenticationManager goes away and uses Shiro, need to figure out how to produce the current golfer
  The auth package goes away and uses Shiro, need to figure out what to do about captcha

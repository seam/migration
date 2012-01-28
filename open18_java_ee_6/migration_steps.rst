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

      <groupId>org.jboss.seam.catch</groupId>
      <artifactId>seam-catch-parent</artifactId>
      <packaging>war</packaging>
      <version>3.1.0.Beta-SNAPSHOT</version>
      <name>Open18 application from Seam in Action</name>

      <properties>
      </properties>

      <dependencyManagement>
          <dependencies>

              <dependency>
                  <groupId>org.jboss.spec</groupId>
                  <artifactId>jboss-javaee-6.0</artifactId>
                  <version>${jboss.spec.version}</version>
                  <type>pom</type>
              </dependency>

          </dependencies>
      </dependencyManagement>

      <dependencies>
          <dependency>
              <groupId>javax.enterprise</groupId>
              <artifactId>cdi-api</artifactId>
              <scope>provided</scope>
          </dependency>
      </dependencies>

  </project>


********************************************************************************
Migrate to JPA 2.0
********************************************************************************

Update persistence.xml to 2.0
================================================================================

.. todo: also take out the transaction manager -- have to double check

Metamodel Generation
================================================================================

.. todo: explain how to create or generate the meta model classes

Migrate to Bean Validation
================================================================================

.. todo: Length(max) -> Max, NotNull is a package change

.. todo: Remove Seam annotations and create producers for them (golfer[session], round[also has a restrict on it, look into this a bit more based on what Dan said])

.. todo: GolferValidator should be a new JSR303 Validator

********************************************************************************
Migrate to CDI
********************************************************************************

.. todo: There is no seam.properties but you will need beans.xml

Substitute Seam 2 annotations for CDI equivalents
================================================================================

Migrate Query  / Home objects to CDI Query
================================================================================

.. todo: Many have restrictions, will have to see how to recreate this.

.. todo: method mapping
  createInstance =>
  isWired =>
  getDefinedInstance =>
  persist => save / saveAndFlush
  remove => remove
  update => refresh

.. todo: Trying to use abstract classes to simply the searching and make it 
  similar to what was done in Seam 2

.. todo: CDI Query DAO objects go into a dao package, new JSF backing bean 
  objects need to be created in the action package.

.. todo: create something to replace roundList and roundCriteria

********************************************************************************
Migrate to  JSF 2.0
********************************************************************************

.. todo: Also will need something to replace CourseComparison
  ProfileAction needs a replacement
  MultiRoundAction needs a Java replacement
  RegisterAction needs a replacement, may be part of switching to Shiro


Update faces-config.xml to 2.0
================================================================================

.. todo: take out the view handler

Migrate to RichFaces 4.1
================================================================================

Rework Navigation from pages.xml
================================================================================

.. todo: also actions and params

Seam Tags and equivalents in JSF and RichFaces
================================================================================

.. todo: s:div, s:fragment, s:link, s:button, s:decorate, s:label, s:span, s:message, s:validateAll, s:convertDateTime, s:convertEnum, s:enumItem, s:selectItems, s:defaultAction 

********************************************************************************
Migrate to Apache Shiro for Security
********************************************************************************

.. todo: AuthenticationManager goes away and uses Shiro, need to figure out how to produce the current golfer
  The auth package goes away and uses Shiro, need to figure out what to do about captcha

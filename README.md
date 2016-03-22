easy-schema
============

A collection of XML schema's used by EASY. These schema's are published at 
[https://easy.dans.knaw.nl/schemas/](https://easy.dans.knaw.nl/schemas/)

Round trip test of ddm.xsd
--------------------------

Goal:
 
* Test an updated ddm.xsd (or the schema's it uses) by depositing via sword-V1 with new examples.
* Verify the ingested metadata.

Prerequisites:

* Access to the legacy code of EASY.

Preparation:

* Make sure to update the version numbers in
  * The schema locations of `easy-schema/src/main/assembly/dist/docs/examples/ddm/`
    (also supply something in these examples that reflects the change in the XSD)
  * `easy-app/lib/ddm/src/main/java/nl/knaw/dans/pf/language/ddm/handlermaps/NameSpace.java`
* `mvn clean install -f easy-schema`
* `mvn -DSNAPSHOT_SCHEMA=true clean install -f easy-app/lib/ddm`
  By default `ddm/target/easy-schema` is copied from the maven repository,
  the system properties copies it from  your own build.
  Replace `clean install` by `process-test-resources` to execute ddm tests by an IDE.

Eat the sword pudding when the updates are accepted and pushed to the
[maven-repo](http://maven-repo.dans.knaw.nl/webapp/search/artifact/?6&q=easy-schema).

  * `mvn clean install -f easy-app/front-end/easy-sword`
  * Go to easy-dtap and execute `./deploy-role.sh easy_sword`
  * Make sure with the web-ui that your easy-user has rights to deposit with sword.
    Also make sure to specify your own email address for the easy-user in your deasy,
    it allows you to verify the EMD created from the DDM
    as presented in the license document sent to the depositor.
  * For convenience the ddm tests create a folder target/swordPuddings. Each pudding is a folder
    with a copy of an example in `DansDatasetMetadata.xml` and some file(s) in the subfolder `data`.
    For each pudding:
    * cd into this folder
    * `zip -r d.zip * ; curl -i --data-binary @d.zip -u `user:password` deasy.dans.knaw.nl/sword/deposit`
    * Check the result on the second tab for the deposited data in the web-ui,
      also check both download formats.
    * Note that the posted ddm become a file in the dataset.

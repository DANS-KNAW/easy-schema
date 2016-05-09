easy-schema
============

A collection of XML schema's used by EASY. These schema's are published at 
[https://easy.dans.knaw.nl/schemas/](https://easy.dans.knaw.nl/schemas/)

Round trip test of ddm.xsd
--------------------------

Purpose:
 
* Test an updated ddm.xsd (or the schema's it uses) by depositing via sword-V1 with new examples.
* Verify the ingested metadata.

Prerequisites:

* Access to the legacy code of EASY.

Preparation:

* Make sure the version numbers are updated in
  * The schema locations of `easy-schema/src/main/assembly/dist/docs/examples/ddm/`
    (also supply something in these examples that reflects the change in the XSD)
  * `easy-app/lib/ddm/src/main/java/nl/knaw/dans/pf/language/ddm/handlermaps/NameSpace.java`
  * `easy-schema/src/main/assembly/dist/docs/sword-v1-packaging.html`
    also update the mapping and help sections as explained in the comment of the file.
* `mvn clean install -f easy-schema`
* `mvn -DSNAPSHOT_SCHEMA=true clean install -f easy-app/lib/ddm`
  By default `ddm/target/easy-schema` is copied from the maven repository,
  the system properties copies it from  your own build.
  Replace `clean install` by `process-test-resources` to execute ddm tests by an IDE.

Eat the sword pudding:

  * Make sure with the web-ui that your easy-user has rights to deposit with sword.
    Also make sure to specify your own email address for the easy-user in your deasy,
    it allows you to verify the EMD created from the DDM
    as presented in the license document sent to the depositor.
  * Use your IDE to execute the easy-sword test class Start with jvm arguments
    `-Xmx1g  -Dwicket.configuration=development -DEASY_SWORD_HOME=xxx -Dlogback.configurationFile=yyy`
  * For convenience the ddm tests create a folder target/swordPuddings. Each pudding is a folder
    with a copy of an example in `DansDatasetMetadata.xml` and some file(s) in the subfolder `data`.
    For each pudding:

    * cd into this folder
    * `zip -r d.zip * ; curl -i --data-binary @d.zip -u` user:password `http://localhost:8083/deposit`

      No need to worry about an internal error if it is just about something like
      `Connection refused for easy-dataset:NNN ...MetadataPidGenerator`
      this means the dataset was created as a draft and the test still serves the purpose.
      When running with a deployed sword service the URL becomes `deasy.dans.knaw.nl/sword/deposit`.

    * Check the result on the second tab for the new dataset in the web-ui,
      also check both download formats.
    * Note that the posted DDM becomes a file in the dataset.

  For now we need a hack for this procedure.
  The easy-sword class RequestContent creates Ddm2EmdCrosswalk without a null argument,
  which means the constructor validates the DDM against the online schema's,
  this looks like an anti pattern that needs a fix.
  As validation is checked by the unit tests in the ddm module
  we can temporarily(!) add a null argument to skip the validation.

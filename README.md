easy-schema
============
[![Build Status](https://travis-ci.org/DANS-KNAW/easy-schema.svg?branch=master)](https://travis-ci.org/DANS-KNAW/easy-schema)

A collection of XML schema's used by EASY. These schema's are published at 
[https://easy.dans.knaw.nl/schemas/](https://easy.dans.knaw.nl/schemas/)


Related maintenance
-------------------

_Prerequisites_: Access to the legacy code of EASY.

The Sword-v1 interface provides a machine-to-machine alternative for the manual deposit procedure of [EASY].
The Sword-v1 [service document] \(accepts any user name with any password, `put` requests do require
proper authentication) refers to the published [packaging document], its source is located next to
the source of the [examples] directory. The `<article id='mapping'>` in the packaging document
contains the DDM-EMD mapping. Subsequent articles show help content of the web forms.

### Sword-v1-packaging document

The mentioned articles are copy pasted from test results of the legacy components `ddm` and `web-ui`,
in that order. These results are located at `target/pageDumps/swordPackagingFragmentHelp.html`.
Note that the help content is generated from [editable] texts in the legacy code base,
the actual versions in production are leading in case of differences.

[EASY]: http://easy.dans.knaw.nl/ui/deposit
[service document]: http://easy.dans.knaw.nl/sword/servicedocument
[packaging document]: https://easy.dans.knaw.nl/schemas/docs/sword-v1-packaging.html
[examples]: https://github.com/DANS-KNAW/easy-schema/tree/master/src/main/assembly/dist/docs
[editable]: https://github.com/DANS-KNAW/easy-app/tree/master/front-end/easy-webui/src/main/assembly/dist/res/example/editable/help


### dtap

When changing the DDM schema, the crosswalker or content of the example(s),
run the round trip test described below,  and use [easy-export-dataset]
to replace the corresponding item(s) in
`easy-dtap/provisioning/roles/easy_test_datasets/files/sdoSets`

[easy-export-dataset]: https://github.com/DANS-KNAW/easy-export-dataset


Round trip test of ddm.xsd
--------------------------

### Purpose:
 
* Test an updated `ddm.xsd` (or the schema's it uses) by depositing via sword-V1 with new/updated examples.
* Verify the ingested metadata.
* Check whether the description tab of a dataset (both view mode as edit mode for archivists) contains less
  than an XML downloaded from the same tab. It would require an update of the [form definition] or even more legacy code to fix such omissions.

[form definition]: https://github.com/DANS-KNAW/easy-app/blob/9ec1c7f0fc496250f797269c874a990d1c21decb/lib/easy-business/src/main/java/nl/knaw/dans/easy/domain/form/form-descriptions/unspecified.xml#L678-L681

### Prerequisites:

* Access to the legacy code of EASY.

### Preparation:

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
* Build and deploy `easy-app/front-end/easy-sword` too

### Eat the sword pudding:

  * Make sure with the web-ui that your easy-user has rights to deposit with sword.
    Also make sure to specify your own email address for the easy-user in your deasy,
    it allows you to verify the EMD created from the DDM
    as presented in the license document sent to the depositor.
  * Use your IDE to execute the easy-sword test class Start with jvm arguments
    `-Xmx1g  -Dwicket.configuration=development -DEASY_SWORD_HOME=xxx -Dlogback.configurationFile=yyy`
  * For convenience the `easy-app/lib/ddm` tests create a folder `target/swordPuddings`. Each pudding is a folder
    with a copy of an example in `DansDatasetMetadata.xml` and some file(s) in the subfolder `data`.
    For each pudding:

    * cd into this folder
    * `zip -r d.zip * ; curl -i --data-binary @d.zip -u` user:password `deasy.dans.knaw.nl/sword/deposit`
    * Check the result on the second tab for the new dataset in the web-ui,
      also check both download formats.
    * Note that the posted DDM becomes a file in the dataset.
    
  * When you want to debug, you can launch the `Start` test class in the easy-sword project
  
    * it needs the jvm arguments
      `-Xmx1g  -Dwicket.configuration=development -DEASY_SWORD_HOME=xxx -Dlogback.configurationFile=yyy`
    * No need to worry about an internal error if it is just about something like
      `Connection refused for easy-dataset:NNN ...MetadataPidGenerator`
      this means the dataset was created as a draft and the test still serves the purpose.
      When running with a deployed sword service the URL becomes `http://localhost:8083/deposit`.

### Work around for not yet published schema's

  The easy-sword class RequestContent creates Ddm2EmdCrosswalk without a null argument,
  which means the constructor validates the DDM against the online schema's.
  As validation is checked by the unit tests in the ddm module
  we can temporarily(!) add a null argument to skip the validation.

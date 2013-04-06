Tutorial: JOSS QuickStart
=========================

This is a working project to demonstrate how to quickly get up to speed with JOSS.

It is built as a standalone application, and contains all content discussed in the accompanying
[blog post by 42 bv](http://blog.42.nl/articles/joss-tutorial-using-joss-to-access-openstack-storage "JOSS Tutorial: using JOSS to access OpenStack Storage").


To see this project in action:
------------------------------

1. Checkout the code
2. Put your account information in src/main/resources/credentials.properties
3. Run this command:
    mvn compile exec:java

All code is in a single class: `nl.tweeenveertig.openstack.tutorial.MainClass`

Works with JOSS v0.8.1

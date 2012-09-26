package nl.tweeenveertig.openstack.tutorial;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.impl.ClientImpl;

/**
 * Simple class to test Joss functionality with.
 *
 * @author <a href="mailto:oscar.westra@42.nl">Oscar Westra van Holthe - Kind</a>
 */
public class MainClass {

    /**
     * Utility class. Do not instantiate.
     */
    private MainClass() {
        // NOthing to do.
    }

    public static void main(String... args) {

        System.out.println("Hello World!");

        // Login

        ResourceBundle credentials = ResourceBundle.getBundle("credentials");
        String tenant = credentials.getString("tenant");
        String username = credentials.getString("username");
        String password = credentials.getString("password");
        String auth_url = credentials.getString("auth_url");
        Account account = new ClientImpl().authenticate(tenant, username, password, auth_url);

        // Add content

        Container myContainer = account.getContainer("MyContainer");
        if (!myContainer.exists()) {
            myContainer.create();
            myContainer.makePublic();
        }

        StoredObject someFile = myContainer.getObject("cloud-computing.jpg");
        someFile.uploadObject(new File("src/main/resources/Cloud-Computing.jpg"));
        System.out.println(someFile.getPublicURL());

        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("Information", "Almost, but not quite, entirely unlike tea.");
        someFile.setMetadata(metadata);


        // List contents

        System.out.printf("Account summary: %d containers containing %d objects with a total of %d bytes%n", account.getContainerCount(),
                          account.getObjectCount(), account.getBytesUsed());
        printMetadata(false, account.getMetadata());

        for (Container container : account.listContainers()) {

            boolean isPublic = container.isPublic();
            System.out.printf("%nContainer: %s (%s, %d objects using %d bytes)%n", container.getName(), isPublic ? "public" : "private",
                              container.getObjectCount(), container.getBytesUsed());
            printMetadata(false, container.getMetadata());

            if (container.getObjectCount() > 0) {

                System.out.println("Contents:");
                for (StoredObject object : container.listObjects()) {

                    System.out.printf("  %s%n", object.getName());
                    if (isPublic) {
                        System.out.printf("    Public URL: %s%n", object.getPublicURL());
                    }
                    System.out.printf("    Type: %s%n    Size: %s%n    Last modified: %s%n    E-tag: %s%n", object.getContentType(), object.getContentLength(),
                                      object.getLastModified(), object.getEtag());
                    printMetadata(true, object.getMetadata());
                }
            }
        }
    }

    private static void printMetadata(boolean useExtraIndent, Map<String, Object> metadata) {
        String indentString = useExtraIndent ? "    " : "";

        System.out.print(indentString);
        if (metadata.isEmpty()) {
            System.out.println("(there is no metadata)");
        } else {
            System.out.println("Metadata:");
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {

                System.out.print(indentString);
                System.out.printf("  %s: %s%n", entry.getKey(), entry.getValue());
            }
        }
    }
}

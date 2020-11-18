package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainXml {
    public static void main(String... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Illegal number of arguments (must be 1)");
        }

        final String PROJECT_NAME = args[0];
        final URL PAYLOAD_URL = Resources.getResource("payload.xml");

        Set<User> users = parseWithJaxb(PROJECT_NAME, PAYLOAD_URL);
        users.forEach(System.out::println);
    }

    private static Set<User> parseWithJaxb(final String PROJECT_NAME, final URL PAYLOAD_URL) {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = null;

        try (InputStream payloadInputStream = PAYLOAD_URL.openStream()) {
            payload = parser.unmarshal(payloadInputStream);
        } catch (JAXBException | IOException e) {
            System.out.println(e.toString());
        }

        if (payload != null) {
            final Project project = payload.getProjects().getProject().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(PROJECT_NAME))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("No project found that matches input: " + PROJECT_NAME));

            final Set<Project.Group> groups = new HashSet<>(project.getGroup());
            return payload.getUsers().getUser().stream()
                    .filter(u -> !Collections.disjoint(u.getGroupRefs(), groups))
                    .collect(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(User::getValue).thenComparing(User::getEmail))));
        } else {
            throw  new IllegalArgumentException("Payload is null");
        }
    }
}

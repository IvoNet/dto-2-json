package nl.ivonet;

import nl.ivonet.dto.People;
import nl.ivonet.dto.Person;
import nl.ivonet.dto.ValidationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;


class Dto2JsonTest {

    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        this.baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.baos));
    }

    @AfterEach
    void tearDown() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
//        System.out.println("baos = " + baos.toString());
    }

    @Test
    void generateDummyJSON() throws IOException {
        Dto2Json.generateDummyJSON(Person.class);
        final var output = this.baos.toString();
        assertThat(output).isEqualTo("""
                {
                  "name" : "String",
                  "surname" : "String",
                  "age" : "Integer",
                  "address" : {
                    "street" : "String",
                    "number" : "String",
                    "city" : "String",
                    "postalCode" : "String"
                  },
                  "alive" : "Boolean",
                  "rich" : "[POOR|RICH]"
                }
                """);
    }

    @Test
    void generateDummyJSON_People() throws IOException {
        Dto2Json.generateDummyJSON(People.class);
        final var output = this.baos.toString();
        assertThat(output).isEqualTo("""
                {
                  "group" : [ {
                    "name" : "String",
                    "surname" : "String",
                    "age" : "Integer",
                    "address" : {
                      "street" : "String",
                      "number" : "String",
                      "city" : "String",
                      "postalCode" : "String"
                    },
                    "alive" : "Boolean",
                    "rich" : "[POOR|RICH]"
                  } ]
                }
                """);
    }

    @Test
    void name() throws IOException {
        Dto2Json.generateDummyJSON(MyRecord.class);
        final var output = this.baos.toString();
        assertThat(output).isEqualTo("""
                {
                  "a" : "String",
                  "b" : "boolean",
                  "c" : "int"
                }
                """);
    }

    record MyRecord(String a, boolean b, int c) {
    }

    @Test
    void genericTest() throws IOException {
        Dto2Json.generateDummyJSON(ValidationResponse.class);
        final var output = this.baos.toString();
        assertThat(output).isEqualTo("""
                {
                  "actionMetadata" : {
                    "metadata" : "String"
                  },
                  "processingData" : [ "DataCommon" ]
                }
                """);
    }
}

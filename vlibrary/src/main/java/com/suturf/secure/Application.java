package com.suturf.secure;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
        title = "Virtual Library",
        version = "1.0",
        description = "Testing Micronaut Security and Swagger",
        license = @License(name = "Apache 2.0"),
        contact = @Contact(url = "http://www.suturf.com/", name = "Suvendra Chakrabarti")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
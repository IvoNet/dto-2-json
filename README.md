# Dto2Json

Sometimes you have a DTO (Data Transfer Object) and you want to convert it to JSON. This is a simple library that does just that.

## Build

- `mvn clean package`

## Usage

- see [Dto2JsonTest](src/test/java/com/github/robtimus/dto2json/Dto2JsonTest.java)
- or `dto2json.sh` script

## More in depth

If you want to use this on a real project, you need to have this script (`dto2json.sh`) available on your path.

### Scenario 

You have a DTO in a project you want to convert to JSON.
- first build that project so that there is a `target/classes` directory and `jar` files have been build
- then run `dto2json.sh` with the fully qualified name of the DTO class as the first argument from the root of the target project.

The shell script will try to add all the needed classes and jars to the classpath so that Dto2Json can find the DTO class. If it succeeds it will print the JSON to stdout.




FROM maven:3.8.1-openjdk-15

# Define working dir with test files
WORKDIR /usr/tests

# Create folders
RUN mkdir -p /usr/tests && \
    mkdir -p /usr/tests/src && \
    mkdir -p /usr/tests/suites \
    mkdir -p /usr/external_files

# Copy source files
COPY src /usr/tests/src

# Copy suites
COPY suites /usr/tests/suites

# Copy main MVN file
COPY pom.xml /usr/tests

# Pre install maven dependencies
RUN mvn install

# Create file with build info
RUN touch buildInfo.json && stamp=$(date) && echo "{\"buildInfo\":\"$stamp\"}" > buildInfo.json

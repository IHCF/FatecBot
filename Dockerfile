FROM openjdk:8
ADD ./fatecbot /usr/src/fatecbot
WORKDIR /usr/src/fatecbot
RUN apt-get update && apt-get install texlive-latex-base -y
CMD ["java", "-jar", "app.jar"]

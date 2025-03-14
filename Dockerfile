# Java 17 기반 이미지 사용
FROM openjdk:17-jdk

# 작업 디렉터리 설정
WORKDIR /app

# Gradle 빌드된 JAR 파일 복사 (Jenkins가 빌드한 JAR 파일 사용)
COPY build/libs/*.jar app.jar

# 컨테이너 실행 시 사용할 포트 설정
EXPOSE 8080

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
package com.codenjoy.dojo.git;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class AddCollaboratorsToRepositories {

    private static final Properties ENV = Env.load("utilities/.env");

    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private static final String OWNER = "codenjoyme";
    private static final String PREFIX = "codenjoy-";

    private static final List<String> CONTRIBUTORS = Arrays.asList("andrii-ua-dev", "maria22101");
    private static final String TEAM_LEAD = "dp-ua";

    private static final String GET_REPOSITORY_COLLABORATORS = "%s/repos/%s/%s/collaborators";
    private static final String GET_USER_REPOSITORIES = "%s/users/%s/repos?type=all&per_page=1000";
    private static final String ADD_COLLABORATOR_TO_REPO = "%s/repos/%s/%s/collaborators/%s";

    private static String token;

    public static void main(String[] args) throws IOException {
        token = ENV.getProperty("GITHUB_TOKEN");

        List<String> repositories = getUserRepositories(OWNER);
        for (String repository : repositories) {
            if (!repository.startsWith(PREFIX)) {
                continue;
            }
            List<String> contributors = getRepositoryCollaborators(OWNER, repository);
            System.out.printf("Repo '%s' contributors [%s]\n", repository, contributors);
            if (!contributors.contains(TEAM_LEAD)) {
                continue;
            }
            for (String contributor : CONTRIBUTORS) {
                if (contributors.contains(contributor)) {
                    continue;
                }
                addCollaboratorToRepo(OWNER, repository, contributor);
            }
        }
    }

    private static List<String> getRepositoryCollaborators(String owner, String repository) throws IOException {
        HttpGet request = new HttpGet(String.format(GET_REPOSITORY_COLLABORATORS,
                GITHUB_API_BASE_URL, owner, repository));
        auth(request);
        JSONArray array = execute(request);
        return getMapToList(array, "login");
    }

    private static List<String> getMapToList(JSONArray jsonArray, String name) {
        return mapToList(jsonArray, item -> item.get(name));
    }

    private static JSONArray execute(HttpRequestBase request) throws IOException {
        HttpResponse response = client().execute(request);
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return new JSONArray(responseBody);
    }

    private static CloseableHttpClient client() {
        return HttpClients.createDefault();
    }

    private static <T> List<T> mapToList(JSONArray jsonArray, Function<Map<String, String>, T> mapper) {
        return jsonArray.toList().stream()
                .map(item -> (Map<String, String>) item)
                .map(mapper)
                .collect(toList());
    }

    private static List<String> getUserRepositories(String owner) throws IOException {
        HttpGet httpGet = new HttpGet(String.format(GET_USER_REPOSITORIES,
                GITHUB_API_BASE_URL, owner));
        JSONArray array = execute(httpGet);
        return getMapToList(array, "name");
    }

    private static void addCollaboratorToRepo(String owner, String repo, String user) throws IOException {
        HttpPut request = new HttpPut(String.format(ADD_COLLABORATOR_TO_REPO,
                GITHUB_API_BASE_URL, owner, repo, user));
        auth(request);
        HttpResponse response = client().execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 201) {
            System.out.printf("Successfully added '%s' to '%s'\n",
                    user, repo);
        } else {
            System.out.printf("Failed to add '%s' to '%s'. Status code: %s\n",
                    user, repo, statusCode);
        }
    }

    private static void auth(HttpRequestBase request) {
        request.setHeader("Authorization", "token " + token);
    }
}
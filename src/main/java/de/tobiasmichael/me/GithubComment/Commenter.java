package de.tobiasmichael.me.GithubComment;


import de.tobiasmichael.me.ResultParser.ResultParser;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;

import java.io.IOException;

/**
 * This class will work against the GitHub API and comment the pull request
 */
public class Commenter {

    private final String comment;

    public Commenter(String comment) {
        this.comment = formatComment(comment);
    }

    public Commenter(String comment, Throwable err) {
        this.comment = formatComment(comment);
        System.out.println(err.toString());
    }


    private String formatComment(String comment) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("___________________\n");
        stringBuilder.append(comment);
        stringBuilder.append("\n___________________\n");

        return stringBuilder.toString();
    }


    public void commentTo() throws IOException {
        String pull_request_number = System.getenv("GITHUB_REF").split("/")[2];
        String repo_owner_and_name = System.getenv("GITHUB_REPOSITORY");

        String oAuthToken = ResultParser.getoAuthToken();
        if (oAuthToken != null) {
            GitHubClient gitHubClient = new GitHubClient();
            gitHubClient.setOAuth2Token(oAuthToken);
            PullRequestService service = new PullRequestService();
            service.getClient().setCredentials("user", "passw0rd");
            RepositoryId repo = new RepositoryId(repo_owner_and_name.split("/")[0], repo_owner_and_name.split("/")[1]);
            CommitComment commitComment = new CommitComment();
            commitComment.setBodyText(comment);
            service.createComment(repo, Integer.parseInt(pull_request_number), commitComment);
        }
    }

}
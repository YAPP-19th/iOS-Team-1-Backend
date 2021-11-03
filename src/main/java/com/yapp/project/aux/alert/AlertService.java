package com.yapp.project.aux.alert;


import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
@Slf4j
public class AlertService {
    @Value(value = "${slack.token}")
    String token;

    @Value(value = "${slack.channel.error}")
    String errorChannel;

    @Value(value = "${slack.channel.batch}")
    String batchChannel;

    public void slackSendMessage(SlackChannel slackChannel, String message){
        try{
            Slack slack = Slack.getInstance();
            String channel;
            if (slackChannel.equals(SlackChannel.ERROR)){
                channel = errorChannel;
            }else{
                channel = batchChannel;
            }
            slack.methods(token).chatPostMessage(req -> req.channel(channel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void slackSendMessage(String message){
        try{
            Slack slack = Slack.getInstance();
            slack.methods(token).chatPostMessage(req -> req.channel(errorChannel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sentryWithSlackMessage(Exception e){
        Sentry.captureException(e);
        slackSendMessage(e.getMessage());
    }


}

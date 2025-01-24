package org.romanzhula.telegram_gpt_bot.telegram.commands.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.romanzhula.telegram_gpt_bot.telegram.components.BotSettings;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramFileService {

    private final BotSettings botSettings;
    private final DefaultAbsSender sender;


    public java.io.File getFile(String fileId) {
        File executeFile = null;

        try {
            executeFile = sender.execute(GetFile.builder()
                    .fileId(fileId)
                    .build()
            );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        String fileUrl = executeFile.getFileUrl(botSettings.getBotToken());

        return getDataFromFileUrl(fileUrl);
    }

    private java.io.File getDataFromFileUrl(String fileUrl) {
        URL url = null;
        try {
            url = new URI(fileUrl).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        java.io.File temporaryfile = null;
        try {
            temporaryfile = java.io.File.createTempFile("telegram", ".ogg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(
            InputStream inputStream = url.openStream();
            FileOutputStream fileOutputStream = new FileOutputStream(temporaryfile)
        ) {
            IOUtils.copy(inputStream, fileOutputStream);

        } catch (IOException e) {
            log.error("File not uploaded: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return temporaryfile;
    }

}

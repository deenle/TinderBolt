package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;


public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = System.getenv("TG_BOT_NAME");
    public static final String TELEGRAM_BOT_TOKEN = System.getenv("TG_BOT_TOKEN");
    public static final String OPEN_AI_TOKEN = System.getenv("OPENAI_TOKEN");

    private DialogMode currentMode = null;
    private final ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private List<String> list = new ArrayList<>();

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        //TODO: основной функционал бота будем писать здесь
        String fileName;
        String message = getMessageText();

        // Allow to communicate only with the bot owner
//        if (!System.getenv("OWNER_ID").isEmpty() &&
//                update.getMessage().getFrom().getId() != Long.parseLong((System.getenv("OWNER_ID")))) return;

        //Show greetings
        if(message.startsWith("/start")) {
            currentMode = DialogMode.MAIN;
            fileName = currentMode.modeToLowerCase();
            sendPhotoMessage(fileName);
            sendTextMessage(loadMessage(fileName));
            return;
        }

        //Talking to ChatGPT
        if(message.startsWith("/gpt")) {
            currentMode = DialogMode.GPT;
            fileName = currentMode.modeToLowerCase();
            sendPhotoMessage(fileName);
            sendTextMessage(loadMessage(fileName));
            return;
        }
        if(currentMode == DialogMode.GPT) {
            String prompt = loadPrompt(DialogMode.GPT.modeToLowerCase());
            String answer = chatGPT.sendMessage(prompt, message);
            sendTextMessage(answer);
            return;
        }

        // Dating
        if(message.startsWith("/date")) {
            currentMode = DialogMode.DATE;
            fileName = currentMode.modeToLowerCase();
            sendPhotoMessage(fileName);

            sendTextButtonsMessage(loadMessage(fileName),
                    "Ариана Гранде \uD83D\uDD25","date_grande",
                    "Марго Робби \uD83D\uDD25\uD83D\uDD25","date_robbie",
                    "Зендея     \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25","date_zendaya",
                    "Райан Гослинг \uD83D\uDE0E","date_gosling",
                    "Том Харди   \uD83D\uDE0E\uD83D\uDE0E","date_hardy");
            return;
        }
        if(currentMode == DialogMode.DATE) {
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("date_")) {
                String prompt = loadPrompt(query);
                chatGPT.setPrompt(prompt);
                sendPhotoMessage(query);
                return;
            }
            String answer = chatGPT.addMessage(message);
            sendTextMessage(answer);
            return;
        }

        //Message a chat to AI
        if(message.startsWith("/message")) {
            currentMode = DialogMode.MESSAGE;
            fileName = currentMode.modeToLowerCase();
            sendPhotoMessage(fileName);
            sendTextButtonsMessage(loadMessage(fileName),
                    "Next message", "message_next",
                    "Invite for a date!", "message_date");
            return;
        }
        if(currentMode == DialogMode.MESSAGE) {
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("message_")) {
                String prompt = loadPrompt(query);
                String userChatHistory = String.join("\n\n", list);
                Message msg = sendTextMessage("Wait pls, I'm thinking...");
                String answer = chatGPT.sendMessage(prompt, userChatHistory);
                updateTextMessage(msg, answer);
                return;
            }
            list.add(message);
            return;
        }

        // Bot menu
        showMainMenu(DialogMode.MAIN.getTitle(), "/start",
                DialogMode.PROFILE.getTitle(), "/profile",
                DialogMode.OPENER.getTitle(), "/opener",
                DialogMode.MESSAGE.getTitle(), "/message",
                DialogMode.DATE.getTitle(), "/date",
                DialogMode.GPT.getTitle(), "/gpt");
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}

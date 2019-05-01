package info.ivicel.springflasky.core;

@FunctionalInterface
public interface MailSender {

    void send(String from, String to);
}

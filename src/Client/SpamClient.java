package Client;

import static java.lang.Integer.parseInt;

class SpamClient extends ChatClient {
    public static void main(String[] args) {
        new SpamClient();
    }

    private void sendSpam(String message){
        String[] data = message.split("&");
        if(data[0].equals("pm")){
            System.out.println("pm");
            sendMessage(message);
        }else if(data[0].equals("spam")){
            try {
                int quantity = parseInt(data[1]);
                for(int i =0; i < quantity; i++){
                    sendMessage("message&" + data[2]);
                }
            }catch (NumberFormatException  exc){
                textArea.append("NaN is input as second parameter.");
            }
        }else{
            System.out.println("message0");
            sendMessage("message&" + message);
        }
    }

    @Override
    void addClickButtonHandler(){
        enterButton.addActionListener(e -> {
            String message = textField.getText();
            if(!message.equals("")) {
                textField.setText("");
                textField.requestFocus();
                System.out.println(message);
                sendSpam(message);
            }
        });
    }

    @Override
    void addPressEnterHandler(){
        textField.addActionListener(e -> {
            String message = textField.getText();
            if(!message.equals("")) {
                textField.setText("");
                System.out.println(message);
                sendSpam(message);
            }
        });
    }
}

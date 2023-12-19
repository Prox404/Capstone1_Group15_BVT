const { ChatOpenAI } = require("langchain/chat_models/openai")
const { OpenAIAssistantRunnable } = require("langchain/experimental/openai_assistant")

class ChatController {

    async Ask(req, res) {
        console.log("call ask");

        const body = req.body;
        const message = body.message;


        if (!(body.message)) {
            return res.status(400).send({ error: "Data not formatted properly" });
        }


        const assistant = new OpenAIAssistantRunnable({
            assistantId: "asst_vL8Feb6YWn7e3x6ZWFLS8OWx",
        });

        await assistant.invoke({
            content: message,
          }).then((result) => {
            console.log("load chain");
            console.log(result);
            res.status(200).send({
                message: result[0].content[0].text.value,
            });
        }).catch((err) => {
            console.log(err);
            res.status(200).send({
                message: "Hệ thống đang bận, vui lòng thử lại sau.",
            });
        });


        console.log("load chain");

    }

    async SideEffect(req, res) {
        console.log("call ask");

        const body = req.body;
        const message = body.message;
        const sideEffect = body.sideEffect;


        if (!(body.message)) {
            return res.status(400).send({ error: "Data not formatted properly" });
        }

        const trainData = `
            Phản ứng sau tiêm chủng : ${sideEffect}
            Dựa vào những thông tin trên, thì phản ứng của bé sau tiêm là: ${message}
            Có được xem là phản ứng tốt hay không, nếu không thì hãy nhắc nhở đến gặp bác sĩ.
        `;

        const model = new ChatOpenAI({
            openAIApiKey: process.env.OPENAI_API_KEY,
            temperature: 0.9,
            modelName: "gpt-3.5-turbo-1106",
            maxTokens: 4000,
        });

        const result = await model.predict(trainData);

        console.log("load chain");


        if (result.length > 0) {
            console.log(result);
            res.status(200).send({
                message: result,
            });
        } else {
            res.status(200).send({
                message: "Hệ thống đang bận, vui lòng thử lại sau.",
            });
        }


    }

}

module.exports = new ChatController;
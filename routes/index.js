const chat = require('./chat');

function route(app) {
    app.use('/ask', chat);
}

module.exports = route
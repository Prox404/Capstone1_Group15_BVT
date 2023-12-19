const express = require('express');
const router = express.Router();

const ChatController = require('../app/controllers/ChatController');

// Store
router.post('/', async (req, res) => {
    ChatController.Ask(req, res);
});

// SideEffect
router.post('/side-effect', async (req, res) => {
    ChatController.SideEffect(req, res);
});


module.exports = router;
const express = require('express')
const morgan = require('morgan')
const route = require('./routes/')


const app = express()

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.raw());
app.use(express.text());
app.use(morgan('combined'));

var cors = require('cors')
const dotenv = require('dotenv');

dotenv.config();
const port = process.env.PORT || 3000

const corsOptions = {
  origin: "*",
  credentials: true, 
  optionSuccessStatus: 200,
};

app.use(cors(corsOptions));


app.get('/', (req, res) => {
  res.send("Hello World")
})

route(app);

app.listen(port || 5000, () => {
  console.log(`Example app listening on port ${port}`)
})

app.listen(8000, function () {
  console.log('CORS-enabled web server listening on port 8000')
})
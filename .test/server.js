/*jshint esversion: 6 */
const express = require("express");
const bodyParser = require("body-parser");
const app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.listen(3000);
console.log("start mock server");

app.get("/getUser", (req, res) => {
    return res.json(userList);
});

app.post("/addUser", (req, res) => {
    console.log(req.body);
    const { user_name, display_name } = req.body;
    if (!user_name || !display_name) {
        return res.json({ status: "failed" });
    }
    userList.push({ user_name: user_name, display_name: display_name });
    return res.json({ status: "success" });
});

const userList = [];

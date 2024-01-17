## The Grambaal Project

![Grambaal logo image](./Grambaal-logo-sm.png)

The Grambaal is the shadow that watches while you sleep, feeding off your nightmares.

Ok, it's really just a hacky Java program for text file-based interaction with the OpenAI chat/completions API.

### Usage
1. run `mvn clean package` to build the jar
2. set GRAMBAAL_API_KEY environment variable to your OpenAI API key
3. empty out your prompt file, then write your latest prompt and save it
3. run `java -jar grambaal-jar-with-dependencies.jar <session name> <prompt file path>`
4. example: `java -jar grambaal-jar-with-dependencies.jar mySession123 ~/myPerfectPrompt.txt`

### What it does
1. if a session with the given name already exists, it will be loaded (~/grambaal/sessions/<session name>.txt)
2. your latest prompt will appended to the session file
3. the entire session file will be submitted to the OpenAI API /chat/completions endpoint
4. the response text content will be appended to the session file
5. the session file will be saved

### More Details for the Curious
1. A session is basically a conversation with the AI.
2. I use simple HTML-inspired delimiters in the session file, which help the AI understand the context and history of the ongoing convo.
3. I actually used this program to work with the AI to write this program!
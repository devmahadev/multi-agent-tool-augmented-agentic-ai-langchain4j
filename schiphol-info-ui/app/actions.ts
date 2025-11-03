"use server";

import { marked } from 'marked';

export async function sendMessage(userMessage: string): Promise<string> {
  const response = await fetch("http://localhost:8080/api/assistant/ask", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ question: userMessage }),
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  const data = await response.json();
  const markdown = data.answer; // Assuming AskResponse has a field `answer`
  const html = marked(markdown);

  return html;
}
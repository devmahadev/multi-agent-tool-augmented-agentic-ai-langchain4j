"use client";

import React, { useState, useCallback, useEffect, useRef } from "react";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import Typography from "@/components/ui/typography";
import { Button } from "@/components/ui/button";
import { Send, Loader2, Bot, User } from "lucide-react";
import { Input } from "@/components/ui/input";
import { sendMessage } from "../actions";

type Msg = { user: string; html: string; time: string };

function ChatHome() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState<Msg[]>([]);
  const [isSending, setIsSending] = useState(false);
  const [showTyping, setShowTyping] = useState(false);

  const inputLength = input.trim().length;
  const canSend = inputLength > 0 && !isSending;

  // Refs for auto-scroll
  const endRef = useRef<HTMLDivElement | null>(null);

  const scrollToBottom = useCallback(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages.length, showTyping, scrollToBottom]);

  const handleSendMessage = useCallback(async () => {
    if (!canSend) return;

    setIsSending(true);
    const userText = input.trim();
    const time = new Date().toLocaleTimeString();

    try {
      // Show the user message immediately (append to end)
      setMessages((prev) => [...prev, { user: "You", html: userText, time }]);
      setInput("");

      // Show typing indicator while waiting
      setShowTyping(true);

      const html = await sendMessage(userText);

      // Append assistant response
      setMessages((prev) => [
        ...prev,
        { user: "Schiphol Info", html, time: new Date().toLocaleTimeString() },
      ]);
    } catch (error) {
      console.error("Error sending message:", error);
      setMessages((prev) => [
        ...prev,
        {
          user: "System",
          html:
            '<div class="text-red-500">Failed to send message. Please try again.</div>',
          time,
        },
      ]);
    } finally {
      setShowTyping(false);
      setIsSending(false);
    }
  }, [canSend, input]);

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    // Enter to send, Shift+Enter for newline (Input is single-line; we prevent default on Enter)
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      if (canSend) handleSendMessage();
    }
  };

  return (
    <div>
      <Card aria-busy={isSending} className="flex h-[75vh] flex-col">
        {/* Indeterminate loading bar (visible only when sending) */}
        {isSending && (
          <div className="relative h-1 w-full overflow-hidden rounded-t-md">
            <div className="absolute inset-0 bg-muted" />
            <div className="absolute inset-y-0 left-0 w-1/3 animate-[loadingSlide_1.2s_ease-in-out_infinite] rounded-r bg-primary" />
          </div>
        )}

        <CardHeader className="pb-2">
          <Typography element="h4" as="h4">
            Chat
          </Typography>
          <p className="text-sm text-muted-foreground">
            Chat
            Gate Agent Assistant – Real-time gate updates and operational insights
            Ask anything. Press Press <kbd className="kbd">Enter</kbd> to send, <kbd className="kbd ml-1">Shift+Enter</kbd> for a new line.
          </p>
        </CardHeader>

        {/* Conversation area */}
        <CardContent className="no-scrollbar flex-1 space-y-4 overflow-y-auto pb-2">
          {messages.length === 0 && !showTyping ? (
            <div className="mt-10 text-center text-sm text-muted-foreground">
              Start the conversation…
            </div>
          ) : null}

          {messages.map((m, idx) => {
            const isUser = m.user === "You";
            const isAssistant = m.user === "Schiphol Info";
            const isSystem = m.user === "System";

            const alignClass = isUser ? "justify-end" : "justify-start";

            // DISTINCT colors/backgrounds:
            // - User: primary gradient bubble with primary-foreground text
            // - Assistant: subtle muted bubble on left
            // - System: soft amber alert style
            const bubbleClass = isUser
              ? "user-bubble text-primary-foreground"
              : isAssistant
              ? "assistant-bubble text-foreground"
              : "system-bubble text-amber-900 dark:text-amber-200";

            return (
              <div key={idx} className={`flex ${alignClass} items-start gap-2`}>
                {!isUser && (
                  <div className="mt-1 rounded-full bg-muted p-2 text-muted-foreground">
                    {isAssistant ? (
                      <Bot className="h-4 w-4" />
                    ) : (
                      <span className="inline-block h-4 w-4 rounded-full bg-amber-500" />
                    )}
                  </div>
                )}

                <div
                  className={`max-w-[85%] rounded-2xl px-4 py-2 shadow-sm ${bubbleClass}`}
                >
                  <div className="mb-1 text-xs opacity-70">
                    {m.user} · {m.time}
                  </div>

                  {/* User text is safe as text; assistant/system may be HTML */}
                  {isUser ? (
                    <div className="whitespace-pre-wrap break-words">{m.html}</div>
                  ) : (
                    <div
                      className="prose prose-sm dark:prose-invert max-w-none prose-headings:mt-3 prose-p:my-2 prose-ul:my-2 prose-li:my-1"
                      dangerouslySetInnerHTML={{ __html: m.html }}
                    />
                  )}
                </div>

                {isUser && (
                  <div className="mt-1 rounded-full user-avatar p-2 text-primary-foreground">
                    <User className="h-4 w-4" />
                  </div>
                )}
              </div>
            );
          })}

          {/* Typing indicator */}
          {showTyping && (
            <div className="flex items-start gap-2">
              <div className="mt-1 rounded-full bg-muted p-2 text-muted-foreground">
                <Bot className="h-4 w-4" />
              </div>
              <div className="assistant-bubble max-w-[85%] rounded-2xl px-4 py-2 shadow-sm">
                <div className="mb-1 text-xs opacity-70">
                  Schiphol Info · {new Date().toLocaleTimeString()}
                </div>
                <div className="typing">
                  <span className="dot" />
                  <span className="dot" />
                  <span className="dot" />
                </div>
              </div>
            </div>
          )}

          {/* Scroll anchor */}
          <div ref={endRef} />
        </CardContent>

        {/* Composer (sticky footer) */}
        <CardFooter className="mt-auto border-t bg-background/60 backdrop-blur supports-[backdrop-filter]:bg-background/40">
          <div className="flex w-full items-center gap-2 p-2">
            <Input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Type your message..."
              className="m-0"
              onKeyDown={handleKeyDown}
              disabled={isSending}
              aria-disabled={isSending}
            />
            <Button
              onClick={handleSendMessage}
              disabled={!canSend}
              aria-live="polite"
            >
              {isSending ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Sending...
                </>
              ) : (
                <>
                  <Send className="mr-2 h-4 w-4" />
                  Send
                </>
              )}
            </Button>
          </div>
        </CardFooter>
      </Card>

      {/* Local styles */}
      <style jsx>{`
        @keyframes loadingSlide {
          0% {
            transform: translateX(-120%);
          }
          50% {
            transform: translateX(20%);
          }
          100% {
            transform: translateX(120%);
          }
        }
      `}</style>

      {/* Global helper styles for distinct bubbles, avatars, typing indicator & kbd */}
      <style jsx global>{`
        /* User bubble: primary gradient (light/dark aware) */
        .user-bubble {
          background: linear-gradient(
            135deg,
            hsl(var(--primary)) 0%,
            color-mix(in oklab, hsl(var(--primary)) 85%, black) 100%
          );
        }
        .user-avatar {
          background: color-mix(in oklab, hsl(var(--primary)) 18%, transparent);
          border: 1px solid color-mix(in oklab, hsl(var(--primary)) 55%, transparent);
        }

        /* Assistant bubble: subtle neutral surface */
        .assistant-bubble {
          background: color-mix(
            in oklab,
            hsl(var(--muted)) 85%,
            hsl(var(--background))
          );
          border: 1px solid hsl(var(--border));
        }

        /* System bubble: soft amber alert */
        .system-bubble {
          background: color-mix(in oklab, #fef3c7 85%, white);
          border: 1px solid #fcd34d;
        }

        .typing {
          display: inline-flex;
          align-items: center;
          gap: 0.25rem;
          height: 1rem;
          color: hsl(var(--foreground));
        }
        .typing .dot {
          width: 0.35rem;
          height: 0.35rem;
          border-radius: 9999px;
          background: currentColor;
          opacity: 0.4;
          animation: blink 1.2s infinite;
        }
        .typing .dot:nth-child(2) {
          animation-delay: 0.15s;
        }
        .typing .dot:nth-child(3) {
          animation-delay: 0.3s;
        }
        @keyframes blink {
          0%, 80%, 100% { opacity: 0.25; }
          40% { opacity: 0.9; }
        }

        .kbd {
          display: inline-block;
          border: 1px solid hsl(var(--border));
          background: hsl(var(--muted));
          color: hsl(var(--muted-foreground));
          padding: 0 0.35rem;
          border-radius: 0.25rem;
          font-size: 0.75rem;
          line-height: 1.25rem;
        }

        .no-scrollbar::-webkit-scrollbar { display: none; }
        .no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
      `}</style>
    </div>
  );
}

export default ChatHome;
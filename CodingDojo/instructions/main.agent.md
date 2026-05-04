# Instructions Catalog

Each entry below is an instruction file with a one-line description. Optional sub-fields after `+`:
- **Keywords** — trigger words/phrases: if user's request matches, load this instruction.
- **Target** — file glob pattern: if current file or context matches, consider this instruction relevant.
- **Exceptions** — edge cases or clarifications that don't fit in the one-liner.

---

- [`./CodingDojo/instructions/creating-instructions.agent.md`](./creating-instructions.agent.md) — how to create, update, and organize instruction files and the instruction ecosystem.
  + Keywords: create instruction, add instruction, update instruction, install instruction, new instruction, instruction infrastructure

- [`./CodingDojo/instructions/iterative-prompt.agent.md`](./iterative-prompt.agent.md) — iterative prompt workflow: maintain a living `main.prompt.md` file, process `## UPD[N]` blocks, write `### RESULT`, commit, and poll for new updates.
  + Keywords: iterative prompt, UPD, polling, prompt file, main.prompt.md, polling loop, watcher

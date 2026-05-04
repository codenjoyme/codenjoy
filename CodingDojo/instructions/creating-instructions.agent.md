## Disclaimer

When you see `./instructions` it means the `./CodingDojo/instructions` folder in this project. The same applies to any file path starting with `./instructions`.

## Motivation and Use Cases

- IDE-agnostic architecture allows teams to use different IDE/Plugins while sharing same instruction base.
- Team alignment on LLM model choice is more critical than IDE choice - IDE switching is less disruptive.
- Instructions are pure markdown docs describing SDLC workflows without platform-specific adaptors like `alwaysApply: true` or `mode: agent`.
- Following Single Responsibility Principle (SRP) - one SDLC workflow piece per instruction file.
  + Recommended soft limit: ~700 lines per file. Exceeding this is a signal to split.
  + Complex instructions can reference other instructions — composability over monoliths.
  + Terminology hint: large multi-step workflows → "agents", small focused actions → "instructions". Naming may vary by author.
- `main.agent.md` serves as catalog of all instructions with brief descriptions - when asked about (what to do), follow this instruction (with file path).
  + Each entry has optional sub-fields after `+`: **Keywords** (trigger words), **Target** (file glob pattern), **Exceptions** (edge cases).
  + When adding new instruction to catalog, fill in at least Keywords to help model match user requests to instructions.
- Platform-specific entry points (`.github/copilot-instructions.md` for Copilot, `.cursor/rules/*.mdc` for Cursor, `.claude/CLAUDE.md` for Claude Code) reference `main.agent.md` to load with every prompt.
- Optionally, `AGENTS.md` in project root with same content as entry point — universal fallback recognized by Claude, Copilot, Cursor agents.
  + Important when `.github/`, `.cursor/`, or `.claude/` are not committed — without them other non-IDE agents have no entry point to discover `instructions/` folder.
  + Decision is up to the team.
- `instructions/` can live in the project repo or be extracted into a separate sub-repository (git submodule, etc.).
  + `.github/`, `.cursor/` stay local per team member's IDE choice — not committed.
  + For cloud agents — add `AGENTS.md` in project root as described above.
  + Or commit instructions together with the project — simpler, fewer moving parts.
  + Each team decides what fits their workflow.
- Why tool-agnostic over native systems (GitHub `.instructions.md`, Cursor `.mdc`):
  + Native formats are incompatible: Copilot's `applyTo` globs, `excludeAgent` fields, Cursor's `alwaysApply`, `globs`, `description` frontmatter, Claude Code's `paths:` frontmatter in `.claude/rules/*.md` — none of these are portable.
  + Vendor lock-in: rewriting dozens of instruction files when switching IDE or when vendor changes format is wasted effort.
  + This approach: one source of truth in `instructions/`, thin adapter wrappers per IDE (`.github/prompts/*.prompt.md`, `.cursor/rules/*.mdc`, `.claude/commands/*.md`) — only wrappers change on IDE switch.
  + Pure markdown instructions work with any LLM agent (CLI, API, CI pipelines) — not tied to IDE runtime at all.
  + Team members on different IDEs (Copilot, Cursor, Windsurf, etc.) share identical workflow knowledge without translation.
- Architectural advantage — separation of concerns:
  + Instructions = **what to do** (platform-agnostic SDLC knowledge).
  + Wrappers = **how to load** (platform-specific glue, 2–3 lines each).
  + Catalog (`main.agent.md`) = **when to use** (routing by task description).
  + Adding a new IDE means only adding a new set of thin wrappers — zero changes to instruction content.
- Hybrid approach is always welcome: use full power of your IDE's native features (`applyTo`, `globs`, `excludeAgent`, etc.) in wrappers that reference files in `instructions/`. Just keep in mind — those IDE-specific features only work for users of that particular IDE.
- Place instructions where they make sense for your project — then build a tree of `main.agent.md` nodes from root to leaves:
  + Hierarchical layout: `instructions/backend/main.agent.md`, `instructions/frontend/main.agent.md` — each sub-catalog follows same structure, root `main.agent.md` links to them.
  + Alternative: co-located layout — `backend/instructions/*.agent.md`, `frontend/instructions/*.agent.md` — but then IDE entry point must reference multiple roots, which adds complexity.
  + Key idea: tree-shaped chain from IDE entry point (`.github/copilot-instructions.md`, `.cursor/rules/*.mdc`, `.claude/CLAUDE.md`) → root `main.agent.md` → sub-catalogs → leaf instructions.
  + This is context management — model navigates the tree on demand instead of loading all instructions at once, avoiding context overload and interference between unrelated instructions.
- Extract essence from completed chat sessions into new instructions to avoid repeating same troubleshooting in future.
  + After achieving desired outcome through multiple iterations with agent, capture workflow as instruction.
  + Prevents repeating same back-and-forth when similar task appears later.
  + Instructions can be iteratively refined through future usage, triggering on potential model hallucinations.
- Common usage patterns with this instruction:
  + "Following instruction for creating instructions, create instruction based on this chat"
  + "Following instruction for creating instructions, create shortcut-links for all my instructions for Cursor"
  + "Following instruction for creating instructions, update instruction (name) with new knowledge from this chat session"
- Once general idea described in one file, can follow it with light prompt adjustments for different contexts.

## Bootstrap Installation (New Project Setup)

- If this instruction file was shared into an empty agent session in a project that has no instruction infrastructure — **this is a signal to install everything from scratch**.
- Detect which IDE is used by checking folder markers:
  + `.github/` folder present → VSCode + GitHub Copilot
  + `.cursor/` folder present → Cursor
  + `.claude/` folder present → Claude Code (CLI / IDE with Claude Code extension)
  + None present → ask user which IDE they use, then create the appropriate folder structure
- For **VSCode + GitHub Copilot**, create the following:
  + `.github/copilot-instructions.md` — with the standard entry-point content (see VSCode section below)
  + `.github/prompts/` — folder for prompt files
  + `.vscode/settings.json` — with required settings (see VSCode section below)
  + `instructions/main.agent.md` — catalog file listing all available instructions
  + `instructions/creating-instructions.agent.md` — this file itself (copy from source)
- For **Cursor**, create the following:
  + `.cursor/rules/mcpyrex.mdc` — main rules file with entry-point content (see Cursor section below)
  + `.cursor/rules/` — folder for per-instruction rule files
  + `instructions/main.agent.md` — catalog file
  + `instructions/creating-instructions.agent.md` — this file itself
- For **Claude Code**, create the following:
  + `.claude/CLAUDE.md` — project memory file with entry-point content (see Claude Code section below)
  + `.claude/commands/` — folder for custom slash-command files (equivalent to prompt files)
  + `.claude/rules/` — folder for modular path-scoped rule files
  + `instructions/main.agent.md` — catalog file
  + `instructions/creating-instructions.agent.md` — this file itself
- After creating all files, verify:
  + Entry-point file correctly references `./instructions/main.agent.md`
  + `main.agent.md` exists and lists at least `creating-instructions.agent.md`
  + IDE settings/rules are configured to load instructions on every prompt
- Confirm to user: "Instruction infrastructure installed. You can now add more instructions following `creating-instructions.agent.md`."

## General Concepts

- If you don't know exactly which IDE (with which Agent system) is used in the project, there are always the markers described below (`.github/` → Copilot, `.cursor/` → Cursor, `.claude/` → Claude Code).
- Always can create missing components when asked:
  + Make all missing prompts/rules based on existing instructions and selected IDE.
  + Create `main.agent.md` file with proper structure and links
  + Complete any missing instruction ecosystem components
- Instructions are platform-agnostic markdown files that contain pure actionable statements.
- Prompt files are platform-specific wrappers that reference instruction files using appropriate syntax.
- Instruction files contain the core logic, prompt files contain platform-specific integration.

## Instructions 

- If you are asked to create another one - please add it in this folder `./instructions/` as new file.
- All instructions are placed in `./instructions/` folder with `[name].agent.md` extension.
- Here `[name]` should consist of several words separated by a `-` symbol, the first of which is a verb, the essence of the operation being performed. 
- Use bullet points format, avoid headers and sections - keep it simple and actionable.
- Write short, concise statements - minimize words, maximize usefulness.
- Each point should be specific and actionable, not explanatory.
- Add new instruction reference to `./instructions/main.agent.md` with one-line description of what it covers.
- `main.agent.md` format example:
```markdown
# Instructions Catalog

Each entry below is an instruction file with a one-line description. Optional sub-fields after `+`:
- **Keywords** — trigger words/phrases: if user's request matches, load this instruction.
- **Target** — file glob pattern: if current file or context matches, consider this instruction relevant.
- **Exceptions** — edge cases or clarifications that don't fit in the one-liner.

---

- [`./instructions/example.agent.md`](./example.agent.md) — one-line description.
  + Keywords: word1, word2, phrase
  + Target: `src/**/*.ts`, `config.*`
  + Exceptions: does not apply when ...
```
- Use backticks for code examples, file paths, and commands.
- Include practical examples when necessary, but keep them minimal.
- Structure: bullet points with sub-bullets using `+` when needed.
- Avoid long explanations - focus on what to do, not why
- Look at existing files like `create-tool.agent.md`, `create-instruction.agent.md` for style reference.
- Use English for instruction content, respond in user's language.
- Test practical examples before including them.
- Keep file focused on one topic or workflow.
- Apply Single Responsibility Principle to instructions to avoid duplication.
- Extract common workflows into separate reusable instruction files.
- Reference shared instructions using `./instructions/[shared-name].agent.md` format.
- When updating existing instruction files:
  + Read existing file first to understand current structure and content.
  + Check which statements from new requirements already exist in the file.
  + Add new statements without rewriting the entire file using targeted edits.
  + Preserve existing useful content and build upon it incrementally.
  + Use sub-bullets with `+` for detailed practices under main points.
  + Include lessons learned from practical implementation experience.
  + Add debug and maintenance guidance for future development work.
  + Focus on actionable insights that improve workflow efficiency.

## Skills

- Use a **skill** instead of a plain instruction when the workflow includes executable code, reference docs, or reusable assets — not just prose steps.
- Skills follow the [agentskills.io](https://agentskills.io/what-are-skills) format: a self-contained folder with a required `SKILL.md` and optional sub-folders.
- Create skill under `./instructions/[name]/` where `[name]` follows the same verb-first, hyphen-separated naming as instructions (e.g. `run-migration`, `generate-report`).
- Required structure:
  ```
  instructions/[name]/
  ├── SKILL.md          # Required: instructions + metadata
  ├── scripts/          # Optional: executable code (shell, Python, JS, etc.)
  ├── references/       # Optional: documentation, API specs, guides
  └── assets/           # Optional: templates, example files, resources
  ```
- `SKILL.md` must contain:
  + Frontmatter block with at minimum `name`, `description`, and `version`:
    ```markdown
    ---
    name: skill-name
    description: One-line description of what this skill does
    version: 1.0.0
    ---
    ```
  + Step-by-step instructions in the same bullet-point style as regular instructions.
  + References to any scripts or assets using relative paths (e.g. `./scripts/run.sh`).
- Only create sub-folders that are actually needed — do not scaffold empty `scripts/`, `references/`, or `assets/` directories.
- Add skill to `./instructions/main.agent.md` the same way as a regular instruction, but point to `SKILL.md`:
  ```markdown
  - [`./instructions/[name]/SKILL.md`](./[name]/SKILL.md) — one-line description.
    + Keywords: word1, word2
  ```
- For IDE wrappers (VSCode prompts, Cursor rules, Claude commands) reference `./instructions/[name]/SKILL.md` instead of `[name].agent.md`.
- When a skill grows complex enough to need its own sub-catalog, add `instructions/[name]/main.agent.md` and link it from the root catalog.

## VSCode + GitHub Copilot

- You can identify this case by `.github` folder inside your workspace.
- Add new file to the `./.github/prompts/` with name `to-[name].prompt.md` and the line we added to `main.agent.md` as reference to instruction file using platform-specific syntax as in example bellow: 
```markdown
---
agent: agent
---
- When you are asked to _______________, please follow the instructions `./instructions/__________.agent.md`.
```
- The file `.github/copilot-instructions.md` should contain the following:
```markdown
- Important! Always follow the instructions in `./instructions/main.agent.md` file.
- Always load the file completely, not partially.
- It contains links to other files with instructions.
- You should reload it in **every prompt** to get the latest instructions - because of the dynamic nature of the project. 
```
- The settings file `.vscode/settings.json` should contain:
  + Enable instruction and MCP files usage:
  ```
    "github.copilot.chat.codeGeneration.useInstructionFiles": true,
    "chat.mcp.access": "all",
    "chat.agent.maxRequests": 250
  ```
  + [Optionally] Ask user to enable auto-save for better experience:
  ```
    "files.autoSave": "afterDelay",
    "files.autoSaveDelay": 100,
  ```

## Cursor

- You can identify this case by `.cursor` folder inside your workspace.

- You can identify this case by `.cursor` folder inside your workspace.
- Add new file to the `./.cursor/rules/` with name `to-[name].mdc` and reference to instruction file using Cursor-specific syntax:
```markdown
---
description: Brief description of when to use this instruction
globs:
alwaysApply: true
---

Follow the instructions in `./instructions/to-[name].agent.md` when you are asked to _______________.
```
- The main rules file `.cursor/rules/mcpyrex.mdc` should contain the following:
```markdown
---
description: Main instruction orchestrator for the project
globs:
alwaysApply: true
---

- Important! Always follow the instructions in `./instructions/main.agent.md` file.
- Always load the file completely, not partially.
- It contains links to other files with instructions.
- You should reload it in **every prompt** to get the latest instructions - because of the dynamic nature of the project.
```

## Claude Code

- You can identify this case by `.claude` folder inside your workspace.
- Add new file to `./.claude/commands/` with name `to-[name].md` and reference to instruction file:
```markdown
Follow the instructions in `./instructions/[name].agent.md`.

$ARGUMENTS
```
- `$ARGUMENTS` is a special placeholder — gets replaced with user input after the slash command. User invokes via `/project:to-[name]`.
- The project memory file `.claude/CLAUDE.md` should contain the following:
```markdown
- Important! Always follow the instructions in `./instructions/main.agent.md` file.
- Always load the file completely, not partially.
- It contains links to other files with instructions.
- You should reload it in **every prompt** to get the latest instructions - because of the dynamic nature of the project.
```
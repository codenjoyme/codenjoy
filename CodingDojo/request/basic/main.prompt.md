<follow>
iterative-prompt.agent.md
</follow>

## UPD1

Вот отсюда установи мне 2 инструкции
.\vibecoding-for-managers\instructions
iterative prompt
create instructions (это прочитай и установи как положено)
Потом создай request папку и в ней подпапку basic и в ней main.prompt.md и туда в UPD1 перенеси этот мой запрос и там отчитайся, а потом зависни в polling режиме

### RESULT

Установлена instruction infrastructure для VSCode + GitHub Copilot:

- [instructions/iterative-prompt.agent.md](../../instructions/iterative-prompt.agent.md) — скопирован из source
- [instructions/creating-instructions.agent.md](../../instructions/creating-instructions.agent.md) — скопирован из source
- [instructions/main.agent.md](../../instructions/main.agent.md) — каталог инструкций
- [.github/copilot-instructions.md](../../.github/copilot-instructions.md) — entry point, ссылается на main.agent.md
- [.github/prompts/to-iterative-prompt.prompt.md](../../.github/prompts/to-iterative-prompt.prompt.md) — VSCode wrapper
- [.github/prompts/to-creating-instructions.prompt.md](../../.github/prompts/to-creating-instructions.prompt.md) — VSCode wrapper
- [.vscode/settings.json](../../.vscode/settings.json) — useInstructionFiles, maxRequests: 2500, autoSave
- [request/basic/main.prompt.md](main.prompt.md) — этот файл, запрос перенесён в UPD1
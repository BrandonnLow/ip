# Pingpong User Guide

Pingpong is a **desktop app for managing tasks**, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Pingpong can get your task management done faster than traditional GUI apps.

![Ui](Ui.png)

## Quick Start

1. Ensure you have Java 17 or above installed on your Computer.
2. Download the latest `pingpong.jar` from [here](https://github.com/BrandonnLow/ip/releases).
3. Copy the file to the folder you want to use as the home folder for Pingpong.
4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar pingpong.jar` command to run the application.
5. Type commands in the command box and press Enter to execute them.
6. Refer to the [Features](#features) below for details of each command.

## Features

### Notes about command format:
- Words in `UPPER_CASE` are parameters to be supplied by the user.
- Items in square brackets are optional.
- Items with `...` can be used multiple times.

### Adding a todo: `todo`
Adds a todo task to your list.

Format: `todo DESCRIPTION`

Example:
- `todo Buy milk`

### Adding a deadline: `deadline`
Adds a task with a deadline.

Format: `deadline DESCRIPTION /by DATE`

Example:
- `deadline Submit report /by 2025-09-15`

### Adding an event: `event`
Adds an event with start and end times.

Format: `event DESCRIPTION /from DATETIME /to DATETIME`

Example:
- `event Project meeting /from 2025-09-10 1400 /to 2025-09-10 1600`

### Listing all tasks: `list`
Shows all tasks in your list.

Format: `list`

### Marking a task as done: `mark`
Marks the specified task(s) as completed.

Format: `mark INDEX [INDEX2 INDEX3...]`

Examples:
- `mark 2` - marks the 2nd task as done
- `mark 1 3 5` - marks multiple tasks as done

### Unmarking a task: `unmark`
Marks the specified task(s) as not done.

Format: `unmark INDEX [INDEX2 INDEX3...]`

### Finding tasks: `find`
Finds tasks by keyword or date.

Format: `find KEYWORD` or `find DATE`

Examples:
- `find meeting` - finds tasks containing "meeting"
- `find 2025-09-10` - finds tasks on this date

### Deleting a task: `delete`
Deletes the specified task(s) from the list.

Format: `delete INDEX [INDEX2 INDEX3...]`

Examples:
- `delete 3` - deletes the 3rd task
- `delete 1 2 4` - deletes multiple tasks

### Updating a task: `update`
Updates an existing task's details.

Format: `update INDEX [/desc DESCRIPTION] [/by DATE] [/from DATETIME] [/to DATETIME]`

Examples:
- `update 1 /desc Buy groceries and milk`
- `update 2 /by 2025-09-20`

### Adding multiple todos: `addmultiple`
Adds multiple todo tasks at once.

Format: `addmultiple DESCRIPTION1; DESCRIPTION2; DESCRIPTION3`

Example:
- `addmultiple Buy milk; Call mom; Read book`

### Exiting the program: `bye`
Exits Pingpong.

Format: `bye`

## Command Summary

| Action | Format | Example |
|--------|--------|---------|
| Add Todo | `todo DESCRIPTION` | `todo Read book` |
| Add Deadline | `deadline DESCRIPTION /by DATE` | `deadline Submit essay /by 2025-09-15` |
| Add Event | `event DESCRIPTION /from DATETIME /to DATETIME` | `event Meeting /from 2025-09-10 1400 /to 2025-09-10 1600` |
| List | `list` | `list` |
| Mark | `mark INDEX` | `mark 2` |
| Unmark | `unmark INDEX` | `unmark 2` |
| Delete | `delete INDEX` | `delete 3` |
| Find | `find KEYWORD` | `find project` |
| Update | `update INDEX [fields]` | `update 1 /desc New description` |
| Add Multiple | `addmultiple DESC1; DESC2` | `addmultiple Task 1; Task 2` |
| Exit | `bye` | `bye` |

## Data Storage
Pingpong data is saved automatically to `./data/pingpong.txt`. There is no need to save manually.
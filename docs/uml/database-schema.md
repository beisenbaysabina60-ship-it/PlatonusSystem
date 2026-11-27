# Database schema

Негізгі кестелер `DatabaseInitializer` класында SQLite арқылы жасалады:

- `users` — ортақ пайдаланушы деректері; `id` — PK, `email` — UNIQUE.
- `students` — `users.id` арқылы пайдаланушыға байланысады; `student_number` — UNIQUE.
- `teachers` — `users.id` арқылы пайдаланушыға байланысады.
- `admins` — `users.id` арқылы пайдаланушыға байланысады.
- `courses` — пәндер; `course_code` — UNIQUE, `credits > 0`.
- `student_groups` — оқу топтары; `group_name` — UNIQUE.
- `lessons` — `courses`, `teachers`, `student_groups` кестелеріне сыртқы кілттер арқылы байланысады.
- `attendance` — `students` және `lessons` кестелеріне байланысады; `(student_id, lesson_id)` UNIQUE.
- `grades` — `students` және `courses` кестелеріне байланысады; `(student_id, course_id)` UNIQUE және `score` 0–100 аралығында.

Дерекқордың нақты DDL көзі: `src/main/java/kz/platonus/database/DatabaseInitializer.java`.

# Project Rules (`DogApp`)

1. **Keep `README.md` synchronized:** Whenever a change affects anything documented in `README.md` (dependencies, SDK/Gradle versions, architecture, API endpoints, repository structure, user stories status, or project links), update `README.md` accurately in the same task.
2. **Always use Data Binding:** Always connect XML layouts to Kotlin code using Android Data Binding (`<layout>` root tag in XML and the generated binding classes in Activities/Fragments/Adapters). Do not use `findViewById` or plain View Binding instead of Data Binding.
3. **Follow the official color palette:** Always use the color tokens documented in `COLOR_PALETTE.md` and defined in `app/src/main/res/values/colors.xml` (`@color/...`). Never hardcode hex color values in XML layouts, drawables, or Kotlin code.
4. **Follow the course style:** Before writing code, read `COURSE_STYLE_GUIDE.md`. Only use topics seen in class and follow the professor's coding patterns described there.

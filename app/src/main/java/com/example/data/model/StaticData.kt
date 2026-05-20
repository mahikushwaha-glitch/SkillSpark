package com.example.data.model

object StaticData {
    val lessonsList = listOf(
        Lesson(
            id = 1,
            title = "Excel Formulas: VLOOKUP & XLOOKUP",
            category = "Productivity",
            description = "Search and reference data across spreadsheet sheets like a professional data analyst.",
            xpReward = 100,
            badgeUnlocked = "Excel Rookie",
            slides = listOf(
                LessonSlide(
                    title = "The Search Superpower",
                    content = "In Excel, finding a matching value across lists is a superpower. VLOOKUP search is widely used, but XLOOKUP is the modern, simpler standard that searches in any direction.",
                    example = "=VLOOKUP(lookup_val, range, col_index, [exact_match])\n=XLOOKUP(lookup_val, lookup_col, return_col)",
                    tip = "Use TRUE or 1 for approximate match, FALSE or 0 for exact match in VLOOKUP."
                ),
                LessonSlide(
                    title = "Unlocking XLOOKUP",
                    content = "XLOOKUP eliminates complex index counts and column order limit issues. You only show what to lookup, where to find it, and what column to return. Clean, bulletproof, and fast.",
                    example = "=XLOOKUP(\"Employee123\", A:A, C:C)",
                    tip = "XLOOKUP is available in modern Microsoft 365 and Excel Web versions."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which keyboard shortcut triggers Flash Fill in Microsoft Excel?",
                    options = listOf("Ctrl + F", "Ctrl + E", "Ctrl + Shift + Enter", "Alt + F4"),
                    correctAnswerIndex = 1,
                    explanation = "Ctrl + E is the magic shortcut for Flash Fill. It fills data automatically based on patterns!"
                )
            )
        ),
        Lesson(
            id = 2,
            title = "Write Impactful Professional Emails",
            category = "Communication",
            description = "A 2-minute formula to write concise, clear, and high-response emails to managers & clients.",
            xpReward = 100,
            badgeUnlocked = "Communication Pro",
            slides = listOf(
                LessonSlide(
                    title = "The Golden Email Rule",
                    content = "No one likes reading essays. Keep business emails to under 150 words. A standard impact email uses exactly four short paragraphs.",
                    example = "1. Context / Warmth\n2. Key Update\n3. Actionable Question\n4. Deadline / Wrap-up",
                    tip = "Break dense text chunks into clear bullet points. Avoid walls of text."
                ),
                LessonSlide(
                    title = "Subject Lines that Win",
                    content = "Write action-oriented, clear subject lines that tell the recipient exactly what is inside. Use brackets like [ACTION REQUIRED] or [URGENT] to establish urgency.",
                    example = "Bad: 'Update'\nGood: '[Action Required] Review Q2 Budget (Due Friday)'",
                    tip = "Never send an email without a clear, specific subject line."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What does the abbreviation 'BLUF' stand for?",
                    options = listOf("Brief Layout Under Format", "Business Launch Up Flow", "Bottom Line Up Front", "Better Layout and User Feedback"),
                    correctAnswerIndex = 2,
                    explanation = "BLUF stands for Bottom Line Up Front. It ensures busy professionals see the primary takeaway immediately."
                )
            )
        ),
        Lesson(
            id = 3,
            title = "Perfect Prompting for ChatGPT",
            category = "Tech Skills",
            description = "Master Role, Context, Constraints, and Examples to extract elite-grade, precise results from AI.",
            xpReward = 100,
            badgeUnlocked = "AI Wizard",
            slides = listOf(
                LessonSlide(
                    title = "Persona Prompting",
                    content = "If you don't give the LLM a persona, it answers in a generic, flat way. Always start your prompt by assigning a specific professional role.",
                    example = "Bad: 'Write email.'\nGood: 'Act as a senior marketing strategist with 15 years experience at Apple...'",
                    tip = "The persona sets the tone, vocabulary, and response depth."
                ),
                LessonSlide(
                    title = "The CREATE Framework",
                    content = "Use this structured framework to get amazing AI responses: Character, Request, Examples, Adjustment, Type of format, Extreme limits.",
                    example = "Character: Python Expert\nRequest: Write simple regex\nType: Single block of clean Python code",
                    tip = "Extreme limits prevent unwanted complications."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary benefit of Few-Shot Prompting?",
                    options = listOf("It consumes fewer API tokens", "It provides concrete examples for AI to mimic", "It bypasses the system instructions", "It disables AI safety rules"),
                    correctAnswerIndex = 1,
                    explanation = "Few-shot prompting shows the exact structure and style of output you expect, yielding precise formatting!"
                )
            )
        ),
        Lesson(
            id = 4,
            title = "60-Second Elevator Pitch",
            category = "Communication",
            description = "Structure your personal value or project idea into an additive 4-step verbal rocket.",
            xpReward = 100,
            badgeUnlocked = "Charismatic Pitcher",
            slides = listOf(
                LessonSlide(
                    title = "The 60-Second Blueprint",
                    content = "An elevator pitch is not a speech. It is a key to unlock conversation. Structure it with 4 quick elements: Hook (10s), Problem (15s), Solution (20s), Call-to-Action (15s).",
                    example = "1. Did you know 80% of managers...? (Hook)\n2. Busy managers suffer from...\n3. We built a micro-app...\n4. Can I buy you a coffee on Tuesday?",
                    tip = "Keep it simple and avoid high-level technical jargon."
                ),
                LessonSlide(
                    title = "The Hook",
                    content = "Start with a surprising fact, a relatable question, or an emotional truth. Draw them in instantly so they look up from their phones.",
                    example = "Hook: 'Traditional online courses have a 95% dropout rate due to bad pacing.'",
                    tip = "A quantitative shock or a major pain point makes a great hook."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the main goal of an elevator pitch?",
                    options = listOf("To ask for business investment immediately", "To read a complete executive summary doc of your startup", "To earn a follow-up conversation or schedule a meeting", "To prove you are smart and speak for an hour"),
                    correctAnswerIndex = 2,
                    explanation = "An elevator pitch is a teaser meant to spark curiosity and earn you a full business meeting!"
                )
            )
        ),
        Lesson(
            id = 5,
            title = "Personal Finance: 50-30-20 Rule",
            category = "Finance",
            description = "Get full control of your money and build wealth without complex accounting software.",
            xpReward = 120,
            badgeUnlocked = "Wealth Builder",
            slides = listOf(
                LessonSlide(
                    title = "The 50/30/20 Formula",
                    content = "A simple, timeless budget split proposed by Senator Elizabeth Warren: 50% on Needs (Rent, food, utilities), 30% on Wants (Dine out, travel, hobbies), 20% on Savings & Debt.",
                    example = "Net income: $50,000\nNeeds: $25,000\nWants: $15,000\nSavings: $10,000",
                    tip = "Automate the 20% savings transfer to execute on payday before you spend."
                ),
                LessonSlide(
                    title = "The Power of Compound Interest",
                    content = "Investing money at a historical return rate results in heavy compound interest over a long period. The earlier you start, the exponentially larger the wealth.",
                    example = "Time is your biggest ally. Delaying starting by just 5 years can shave 50% off final returns.",
                    tip = "Minimize high-interest credit card debt immediately as it compounds against you!"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Under the 50/30/20 rule, which category does dining out at a luxury restaurant belong to?",
                    options = listOf("Needs", "Savings", "Wants", "Essentials"),
                    correctAnswerIndex = 2,
                    explanation = "Dining out at a luxury location is a Want. Basic grocery shopping is a Need, but dining out is for leisure!"
                )
            )
        ),
        Lesson(
            id = 6,
            title = "Resume Writing: Match ATS Bots",
            category = "Career Growth",
            description = "Format your resume to bypass Robot Screening systems and land high-paying job interviews.",
            xpReward = 110,
            badgeUnlocked = "Career Growth Master",
            slides = listOf(
                LessonSlide(
                    title = "What is an ATS?",
                    content = "90% of Fortune 500 companies use an Applicant Tracking System (ATS). It is a machine learning filter that screens resumes for keywords before a human ever sees them.",
                    example = "If your resume uses complex multi-column tables, graphics, or bad fonts, the ATS bot reads it as gibberish and trashes it automatically.",
                    tip = "Use extremely standard, boring single-column doc layouts in MS Word or PDF."
                ),
                LessonSlide(
                    title = "Action-Driven Bullet Points",
                    content = "Do not write lists of regular tasks you did. Write achievements using Google's X-Y-Z formula: Accomplished [X], as measured by [Y], by doing [Z].",
                    example = "Standard: 'Responsible for social media.'\nATS-Killer: 'Increased social click-throughs by 34% (X) in 3 months (Y) by optimizing schedules (Z).'",
                    tip = "Always lead your bullets with strong action verbs like led, optimized, or generated."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "How should a standard resume bullet point be structured to maximize ATS scoring?",
                    options = listOf("List daily tasks", "Showcase positive self-descriptors", "Google's X-Y-Z Formula", "Copy-paste entire text in white"),
                    correctAnswerIndex = 2,
                    explanation = "The XYX Accomplishment formula is highly quantitative and tells the algorithmic screening system exactly how you add value!"
                )
            )
        ),
        // === BUSINESS CATEGORY (7 - 15) ===
        Lesson(
            id = 7,
            title = "Lean Startup MVP Model",
            category = "Business",
            description = "Build, measure, and learn quickly with minimal resources to test real market viability.",
            xpReward = 100,
            badgeUnlocked = "MVP Pioneer",
            slides = listOf(
                LessonSlide(
                    title = "The MVP Philosophy",
                    content = "A Minimum Viable Product (MVP) is the simplest version of your idea that allows you to start collecting validated learning with the least effort.",
                    example = "Uber started as a simple app connecting iPhone users with private limo drivers in San Francisco to test demand.",
                    tip = "Ask: What is the single core value proposition that solves the customer's primary pain point?"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary purpose of a Minimum Viable Product (MVP)?",
                    options = listOf("To make high profits instantly", "To gather validated customer learnings with minimal effort", "To show off development skills", "To build a flawless giant product"),
                    correctAnswerIndex = 1,
                    explanation = "An MVP aims to test core assumptions and get real user feedback quickly to avoid wasting resources."
                )
            )
        ),
        Lesson(
            id = 8,
            title = "B2B Sales Outreach Strategy",
            category = "Business",
            description = "A step-by-step approach to high-converting cold email and phone prospecting outreach.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Hook-Value-Ask Structure",
                    content = "Cold emails fail because they talk about the salesperson. Successful outreach hooks interest, provides hyper-relevant value, and finishes with a low-friction action request.",
                    example = "Hi [Name], loved your campaign on [X]. We built a fast optimizer that could save you 4 hours weekly. Active on Tuesday for a 5min chat?",
                    tip = "Never use long templates. Personalize the first 2 lines for every lead."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the most effective approach for B2B cold emails?",
                    options = listOf("Attach a 40-page PDF", "Focus heavily on personalization and brief value proposition", "Send the same template to 10,000 random emails", "Ask them to sign a contract on the spot"),
                    correctAnswerIndex = 1,
                    explanation = "Personalized and brief emails that highlight quick customer benefits get much higher engagement rates."
                )
            )
        ),
        Lesson(
            id = 9,
            title = "Product Market Fit Formula",
            category = "Business",
            description = "Discover how to pinpoint, measure, and scale severe market demand for your products.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Sean Ellis PMF Test",
                    content = "Product Market Fit (PMF) means being in a good market with a product that can satisfy that market. You measure it by surveying users: 'How would you feel if you could no longer use this product?'",
                    example = "If 40% or more of respondents say they would be 'very disappointed', you have achieved early PMF.",
                    tip = "Stop scaling user acquisition before achieving PMF, or you will suffer high user churn."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "According to Sean Ellis, what percentage of users must feel 'very disappointed' to indicate early Product-Market Fit?",
                    options = listOf("10%", "25%", "40%", "90%"),
                    correctAnswerIndex = 2,
                    explanation = "The 40% 'very disappointed' threshold is the widely recognized benchmark for achieving early Product-Market Fit."
                )
            )
        ),
        Lesson(
            id = 10,
            title = "The Art of Pricing: Value-Based",
            category = "Business",
            description = "Stop selling your physical hours; align pricing directly with customer-perceived value.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Perceived Value Pricing",
                    content = "Value-based pricing sets rates based on the customer's perceived worth of the solution instead of the hours spent building it. This aligns profit incentive with high quality outputs.",
                    example = "Charging a flat $10,000 for a website that increases client sales by $100,000, instead of $50/hour for 20 hours.",
                    tip = "Always ask clients about the financial cost of their unresolved problem."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is Value-Based pricing?",
                    options = listOf("Pricing by counting lines of code", "Setting prices based on client-perceived outcome worth", "Underpricing all competitors to win bids", "Multiplying resource costs by exactly two"),
                    correctAnswerIndex = 1,
                    explanation = "Value-based pricing captures premium profits by pricing based on the financial and emotional value delivered to the buyer."
                )
            )
        ),
        Lesson(
            id = 11,
            title = "Customer Acquisition Cost (CAC)",
            category = "Business",
            description = "Optimize CAC and Lifetime Value (LTV) ratios to ensure healthy startup finance model.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The CAC/LTV Ratio",
                    content = "CAC is the total sales and marketing cost divided by the number of clients acquired. Customer Lifetime Value (LTV) is the total cash earned from a user over active relationship cycle.",
                    example = "Spend $1000 in ads, get 10 customers -> CAC is $100. If each customer averages $300 spend -> LTV is $300.",
                    tip = "A healthy growing SaaS model requires an LTV that is at least 3 times greater than its CAC."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the recommended health ratio of LTV to CAC for a growing software business?",
                    options = listOf("1:1", "1:3", "3:1 or higher", "100:1"),
                    correctAnswerIndex = 2,
                    explanation = "An LTV/CAC ratio of 3x or higher is critical to ensure that gross margins fully cover operations and lead to profitability."
                )
            )
        ),
        Lesson(
            id = 12,
            title = "Deconstruct Your Competitors",
            category = "Business",
            description = "Ethical competitive intelligence templates to analyze rivals and exploit business gaps.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Finding Blue Ocean Gaps",
                    content = "To map competitors, sort their offers into categories. Look for negative customer reviews, missing functionalities, and complex support systems to discover a Blue Ocean gap.",
                    example = "Finding that major software has terrible mobile layout gives you a perfect wedge point.",
                    tip = "Read competitor negative feedback on platforms like Trustpilot, G2, or App Store."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Where is the best place to source competitor product weaknesses ethically?",
                    options = listOf("Hacking their databases", "Reading public negative customer reviews on G2 or Trustpilot", "Asking internal employees to breach contracts", "Making up fake complaints on social media"),
                    correctAnswerIndex = 1,
                    explanation = "Analyzing public reviews helps you find genuine customer struggles to formulate your unique feature solutions."
                )
            )
        ),
        Lesson(
            id = 13,
            title = "Modern Marketing's 4 Ps",
            category = "Business",
            description = "Align Product, Price, Place, and Promotion for modern internet-scale audiences.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Marketing Mix",
                    content = "The 4 Ps of marketing are Product (your offering), Price (your monetization model), Place (your distribution channel), and Promotion (your awareness campaigns). All 4 must harmonize to capture conversion.",
                    example = "Selling premium tech consulting (Product + Price) on LinkedIn (Place) via helpful blogs (Promotion).",
                    tip = "If sales are slow, test changing one of the Ps first before altering overall business direction."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which of the following is NOT one of the traditional 4 Ps of the marketing mix?",
                    options = listOf("Product", "Permission", "Promotion", "Place"),
                    correctAnswerIndex = 1,
                    explanation = "The 4 Ps are Product, Price, Place, and Promotion."
                )
            )
        ),
        Lesson(
            id = 14,
            title = "Read Corporate Income Statements",
            category = "Business",
            description = "Unravel corporate finance: Revenue, OpEx, EBITDA, and Net margins translated simply.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Understanding the Top & Bottom Lines",
                    content = "The top line is Gross Revenue (total income). Then you subtract product costs, operational expenses (OpEx), taxes, and interest to reach the bottom line: Net Income (actual profit).",
                    example = "$100,000 Sales (Top) -> Minus $60,000 Expenses -> $40,000 Net Profit (Bottom).",
                    tip = "Revenue is vanity, profit is sanity, cash flow is reality!"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What represents the 'bottom line' on a financial income statement?",
                    options = listOf("Total sales volume", "Salaries paid to directors", "Net income (profit) after all deductions", "Value of warehouse land assets"),
                    correctAnswerIndex = 2,
                    explanation = "Net income is the absolute final profit left over after matching every possible expense against revenue."
                )
            )
        ),
        Lesson(
            id = 15,
            title = "High-Ticket Commercial Negotiation",
            category = "Business",
            description = "Structure elite service proposals and win key corporate clients without price wars.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Anchoring Trick",
                    content = "During client negotiation, anchor high first with different tier structures. Show premium outcomes alongside standard scopes. This keeps discussions centered on value rather than cheap pricing.",
                    example = "Offering packages at $15k, $8k, and $4k instead of a single $5k quote. This shifts choice from Yes/No to 'Which package is best?'",
                    tip = "Never negotiate prices directly. If the client demands lower fee, decrease the project scope of delivery."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "If a prospect requests a price discount, how should an elite service negotiator respond?",
                    options = listOf("Immediately agree to build relationships", "Refuse to talk to them anymore", "Reduce the project scope of work proportionally to the lower price", "State a random higher price to confuse them"),
                    correctAnswerIndex = 2,
                    explanation = "Reducing the scope of work allows you to uphold the integrity of your regular rate while accommodating budget limits."
                )
            )
        ),

        // === LEARNING CATEGORY (16 - 24) ===
        Lesson(
            id = 16,
            title = "Feynman Technique for Fast Study",
            category = "Learning",
            description = "The ultimate hyper-learning heuristic: explain complex ideas to a 5-year old.",
            xpReward = 100,
            badgeUnlocked = "Fast Learner",
            slides = listOf(
                LessonSlide(
                    title = "Simplify to Understand",
                    content = "Named after Nobel physicist Richard Feynman. To learn any idea: study it, write an explanation in super plain language as if explaining to a 5-year old, note where you get stuck, and look up original answers.",
                    example = "Explaining cryptocurrency as: 'A shared digital diary of coin trades that everyone holds a copy of so nobody can lie.'",
                    tip = "Use simple analogies instead of complex technical jargon to expose gaps in your real comprehension."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the core action of the Richard Feynman Learning Technique?",
                    options = listOf("Read textbook 5 times", "Explain the concept in simple, jargon-free child terms", "Hire a master PhD private teacher", "Memorize formulas via acronyms"),
                    correctAnswerIndex = 1,
                    explanation = "Simplifying explanation forces your mind to form logical bridges, highlighting exactly where your comprehension falls short."
                )
            )
        ),
        Lesson(
            id = 17,
            title = "Active Recall & Spacing Systems",
            category = "Learning",
            description = "Defeat memory decay using testing intervals configured against forgetting curves.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Active vs Passive Methods",
                    content = "Rereading notes is passive and yields rapid forgetting. Active recall involves closing your notes and forcing your active brain neurons to retrieve answers. Spacing revisits content in 1-day, 3-day, 7-day, and 14-day gaps.",
                    example = "Flashcard systems (like Anki) that automatically space card views according to custom performance metrics.",
                    tip = "Test yourself before you feel fully ready. The active struggle locks memory retrieval pathways."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which system utilizes spacing intervals to maximize neural retention?",
                    options = listOf("Mass cramming on exam night", "Spaced Repetition System", "Sub-vocal speed training", "Sub-conscious sleep listening"),
                    correctAnswerIndex = 1,
                    explanation = "Spaced Repetition challenges memory right before it is about to dissolve, maximizing long-term retention."
                )
            )
        ),
        Lesson(
            id = 18,
            title = "The Pareto 80/20 Rule",
            category = "Learning",
            description = "Target the 20% of supreme material that delivers 80% of actual learning progress.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Focus on Critical Assets",
                    content = "In study, 80% of results are generated by 20% of inputs. Focus first on high-frequency vocabulary, core frameworks, or common formulas to hit conversational or functional speed rapidly.",
                    example = "In Spanish, the 100 most common words make up over 50% of spoken conversation.",
                    tip = "Locate high-yield elements by reviewing past examinations or talking to advanced practitioners."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "How does a student apply the Pareto Principle to learning a foreign language?",
                    options = listOf("Translate a complete dictionary", "Focus initially on the high-frequency first 300 words", "Study 8 hours daily without breaks", "Watch random movies in that language with no subtitles"),
                    correctAnswerIndex = 1,
                    explanation = "Studying vital, top-frequency vocabulary lets you unlock functional communication in far less time."
                )
            )
        ),
        Lesson(
            id = 19,
            title = "Speed Reading & Text Synthesis",
            category = "Learning",
            description = "Double your Reading speed with sub-vocalization filters while keeping comprehension intact.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Eliminating the Inner Voice",
                    content = "We read slowly because we pronounce words in our head (sub-vocalization). To increase speed, train your eyes to scan groups of words using visual pacing tools. Keep track using your index finger as a constant guide.",
                    example = "Guide your finger smoothly down the center of paragraphs instead of reading line-by-line horizontally.",
                    tip = "Use a fast-pointer device to train your eye muscle speed."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary cognitive cause of slow book reading speeds?",
                    options = listOf("Poor glasses prescription", "Sub-vocalization (speaking the words in your mind)", "Small text layouts", "Page color gradients"),
                    correctAnswerIndex = 1,
                    explanation = "Sub-vocalization limits your reading velocity to your natural speaking speed (approx 150-250 wpm)."
                )
            )
        ),
        Lesson(
            id = 20,
            title = "First Principles Thinking",
            category = "Learning",
            description = "Break complex situations down to foundational truths to formulate authentic breakthroughs.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Deconstruction & Reconstruction",
                    content = "Rather than reasoning by analogy (copying others), First Principles thinking forces you to deconstruct a topic to its fundamental, undeniable truths. From there, you build up a brand new strategy.",
                    example = "Elon Musk questioned battery prices by buying raw items (lithium, cobalt) and making packs, saving over 80% of retail rates.",
                    tip = "Ask: 'What is undeniably true here?' and ignore social opinions."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is a main action step in First Principles Thinking?",
                    options = listOf("Copying industry leaders", "Unravelling a problem to its basic components and building up", "Consulting social media trends", "Making decisions on gut feelings only"),
                    correctAnswerIndex = 1,
                    explanation = "First principles deconstructs an issue to build custom, highly creative solutions from scratch."
                )
            )
        ),
        Lesson(
            id = 21,
            title = "Build a Digital Second Brain",
            category = "Learning",
            description = "Master Notion/Obsidian to Capture, Organize, Distill, and Express raw intelligence.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The CODE Method",
                    content = "Build an external system to manage knowledge. Tiago Forte proposes the CODE method: Capture (note key ideas), Organize (sort in folders), Distill (summarize notes), Express (create projects with your stored knowledge).",
                    example = "Keep a digital notebook to capture interesting articles instantly on your phone.",
                    tip = "If a note isn't actionable, don't waste time formatting it."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which workflow step in the CODE method focuses on summarizing complex bookmarks?",
                    options = listOf("Capture", "Organize", "Distill", "Express"),
                    correctAnswerIndex = 2,
                    explanation = "Distilling extracts the core essence of information, making it accessible for future projects."
                )
            )
        ),
        Lesson(
            id = 22,
            title = "The 20-Hour Rule for Skills",
            category = "Learning",
            description = "Deconstruct major skills and bypass early friction barriers to learn anything quickly.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Crossing the Frustration Barrier",
                    content = "Josh Kaufman proved it takes 10,000 hours to master a skill, but only 20 hours of focused practice to become reasonably good. Spend the first hour deconstructing the skill into sub-skills and removing key study distractions.",
                    example = "Learning 4 basic guitar chords to play 80% of popular songs in under 2 weeks.",
                    tip = "Pre-commit to practicing for at least 20 hours to push past early cognitive self-doubt."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "How many hours of focused practice are required to cross the early frustration barrier of a new skill?",
                    options = listOf("1 Hour", "20 Hours", "500 Hours", "10,000 Hours"),
                    correctAnswerIndex = 1,
                    explanation = "Kaufman's research shows that 20 hours of deliberate practice delivers functional skill competency."
                )
            )
        ),
        Lesson(
            id = 23,
            title = "Interleaved Learning Strategy",
            category = "Learning",
            description = "Mix different topics during training to build agile cognitive problem-solving reflexes.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Power of Variety",
                    content = "Block learning (focusing on one topic for hours) creates an illusion of mastery. Interleaved practice mixes different skills or types of problems to force the brain to choose the right strategy dynamically.",
                    example = "Math practice that mixes addition, multiplication, and division problems, rather than doing 50 multiplication questions sequentially.",
                    tip = "Interleaving feels harder and slower, but builds much higher actual test performance."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What makes Interleaved Practice superior to blocked, repetitive study?",
                    options = listOf("It is easier physically", "It trains the brain to choose the right strategy dynamically", "It minimizes total revision hours", "It requires complete silence"),
                    correctAnswerIndex = 1,
                    explanation = "Mixing problem styles forces the brain to analyze cues and select appropriate tools, building stronger long-term knowledge."
                )
            )
        ),
        Lesson(
            id = 24,
            title = "Cognitive Mind Mapping",
            category = "Learning",
            description = "Develop associative visual webs of complex structures to lock details into memory.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Visual Association mapping",
                    content = "Line-by-line notes do not mimic neural brain layout. Mind mapping captures ideas visually starting from a central concept and branching out. This uses visual, spatial, and color triggers to improve retention.",
                    example = "Drawing a central bubble for 'Income Statements' and branching into Revenue, Expenses, and Assets.",
                    tip = "Use different colors for branches to trigger visual retention shortcuts."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Why are mind maps more compatible with biological memory?",
                    options = listOf("They are typed in monochrome", "They structure concepts associatively, similar to neural webs", "They ignore text completely", "They replace the need for revision"),
                    correctAnswerIndex = 1,
                    explanation = "Mind mapping mirrors our brain's natural network links, assisting with fast recall."
                )
            )
        ),

        // === GROWTH CATEGORY (25 - 33) ===
        Lesson(
            id = 25,
            title = "Growth Mindset Transition",
            category = "Growth",
            description = "Shift your mental framework to perceive challenging setbacks as inputs to success.",
            xpReward = 100,
            badgeUnlocked = "Growth Warrior",
            slides = listOf(
                LessonSlide(
                    title = "The Power of 'Yet'",
                    content = "Carol Dweck's research identifies two mindsets. A Fixed Mindset believes talents are static. A Growth Mindset views skill as malleable. Simply changing 'I cannot do this' to 'I cannot do this yet' alters how your brain handles struggles.",
                    example = "Seeing a failed coding test as an indicator to adjust practice, rather than proof of low talent.",
                    tip = "Celebrate your focus, strategy, and work progress instead of raw biological intellect."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary belief of a Growth Mindset?",
                    options = listOf("Talents are genetically locked", "Skills and intellectual competency can be built through deliberate effort", "Success is purely determined by luck", "Failure should be ignored and forgotten"),
                    correctAnswerIndex = 1,
                    explanation = "A growth mindset believes challenge is the prime catalyst for building neural talent and capability."
                )
            )
        ),
        Lesson(
            id = 26,
            title = "Atomic Habit Stacking",
            category = "Growth",
            description = "Anchor tiny habits to your current routines to trigger automatic lifestyle shifts.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Stacking Formula",
                    content = "James Clear advises using Habit Stacking to form new routines easily. Anchor your desired action to an established daily custom using the formula: After [Current Habit], I will [New Habit].",
                    example = "After pouring my morning coffee (current), I will open My Learning App and study 1 lesson (new).",
                    tip = "Make the new habit extremely easy (under 2 minutes) to prevent early procrastination."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the golden formula for James Clear's Habit Stacking technique?",
                    options = listOf("Before I sleep, I will write 5 goals", "After [Current Habit], I will [New Habit]", "I will do 100 pushups every single hour", "If I fail, I will pay a fine"),
                    correctAnswerIndex = 1,
                    explanation = "Habit stacking leverages established neurological cues in your brain to install and support new positive habits."
                )
            )
        ),
        Lesson(
            id = 27,
            title = "Dopamine Detox & Re-Focus",
            category = "Growth",
            description = "De-stimulate your brain from infinite feeds to unlock deep working capabilities.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Reset Your Focus Baseline",
                    content = "Infinite scroll feeds flood our brains with dopamine, making standard cognitive tasks feel boring. A dopamine detox involves scheduling blocks of low stimulation to reset your baseline and make focused work enjoyable again.",
                    example = "Going on a 4-hour walk without your smartphone, or blocking social media apps until evening work is complete.",
                    tip = "Put your phone in a drawer in another room. Physical distance reduces the urge to pick it up."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary objective of scheduling low-stimulation focus blocks?",
                    options = listOf("To save battery on mobile devices", "To re-sensitize brain dopamine receptors for better focus", "To isolate yourself from coworkers", "To permanently end social app usage"),
                    correctAnswerIndex = 1,
                    explanation = "Reducing hyper-stimulating activity resets attention baselines, making focus-heavy work enjoyable."
                )
            )
        ),
        Lesson(
            id = 28,
            title = "Kaizen: 1% Daily Compound",
            category = "Growth",
            description = "Realize the astronomical returns of accumulating small daily positive gains over a year.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Power of small gains",
                    content = "Kaizen is the philosophy of continuous small steps of improvement. Improving your work, habits, or style by just 1% every single day results in becoming 37 times better by the end of a year.",
                    example = "1.01 to the power of 365 is equal to 37.7. Conversely, deteriorating 1% daily shifts you down close to zero.",
                    tip = "Focus on building solid habit execution streaks before trying to optimize performance."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the approximate compounding result of improving 1% daily for a full year?",
                    options = listOf("1.5 times better", "5 times better", "37 times better", "1000 times better"),
                    correctAnswerIndex = 2,
                    explanation = "Thanks to compound interest math, minor daily improvements compound over time, yielding dramatic long-term growth (37.7x)."
                )
            )
        ),
        Lesson(
            id = 29,
            title = "Deep Work Deep Focus",
            category = "Growth",
            description = "Plan distraction-free work blocks to operate at maximum cognitive effectiveness.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Deep Work Blueprint",
                    content = "Deemed by Cal Newport, Deep Work is professional activity performed in state of distraction-free concentration that pushes your cognitive limits. High-quality output is equal to Time Spent multiplied by Intensity of Focus.",
                    example = "Working 90 minutes on coding with all notifications muted, vs. working 4 hours with active email checks.",
                    tip = "Plan exactly when and where your deep work block occurs before the day begins."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "According to Cal Newport's Deep Work, how is high-quality output calculated?",
                    options = listOf("Time Spent / Motivation Level", "Standard hours in office", "Time Spent x Intensity of Focus", "Coffee consumed x lines written"),
                    correctAnswerIndex = 2,
                    explanation = "Uninterrupted, high-intensity focus allows you to accomplish complex cognitive work in half the time."
                )
            )
        ),
        Lesson(
            id = 30,
            title = "Expanding Your Comfort Zone",
            category = "Growth",
            description = "Initiate micro-challenges of discomfort to conquer passive self-sabotage.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Voluntary Discomfort Experiments",
                    content = "Confidence is built when we show our nervous system that discomfort is harmless. To expand your comfort zone, run small, micro-challenges of voluntary tension: take cold showers, ask for discounts, or start conversations with strangers.",
                    example = "Ask for a 10% discount on coffee. The rejection is harmless, but the anxiety habit is broken.",
                    tip = "Track your immediate emotional response during these challenges to build objective mental resilience."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary benefit of voluntary discomfort micro-challenges?",
                    options = listOf("Saving lots of money", "Unlocking higher standard intelligence", "Training the brain to handles risk anxiety calmly", "Showing others that you are superior"),
                    correctAnswerIndex = 2,
                    explanation = "Micro-comfort challenges desensitize the amygdala fear response, building confidence for big career challenges."
                )
            )
        ),
        Lesson(
            id = 31,
            title = "Ego Depletion & Willpower",
            category = "Growth",
            description = "Strategize high-stakes focus tasks for times when neural battery is fully charged.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Managing Your Decision Reserve",
                    content = "Willpower is a finite resource that drains with every decision, text block, or emotional constraint you face. To maximize output, schedule highly creative or challenging cognitive tasks during early mornings when your willpower battery is fully charged.",
                    example = "Write complex copy first thing in the morning, rather than after 8 long hours of meetings.",
                    tip = "Simplify daily trivia (like meal prep or clothing choice) to conserve decision-making resource."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "At what point of the day is your neural decision battery generally highest?",
                    options = listOf("Late night after watching TV", "Immediately after complex stressful meetings", "Early morning after restorative sleep", "Directly before lunch break"),
                    correctAnswerIndex = 2,
                    explanation = "Sleep restores glucose reserves in active brain areas, resetting and recharging your physical decision-making capacity."
                )
            )
        ),
        Lesson(
            id = 32,
            title = "Sleep Optimization for Brains",
            category = "Growth",
            description = "Implement 90-minute cycle rules for deep, restorative rest and high waking productivity.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The 90-Minute Cycle Rule",
                    content = "Human sleep occurs in distinct, structured 90-minute periods of light sleep, deep sleep, and REM. Waking up in the middle of a deep sleep stage makes you feel groggy, even if you slept longer. Try to align sleep targets with multiples of 90 minutes.",
                    example = "Targeting exactly 7.5 hours (5 full cycles) or 6 hours (4 full cycles) instead of 7 or 8 hours.",
                    tip = "Stop looking at blue-light electronic screens at least 60 minutes before bedtime."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "How long is a standard, complete biological human sleep cycle?",
                    options = listOf("30 Minutes", "90 Minutes", "4 Hours", "8 Hours"),
                    correctAnswerIndex = 1,
                    explanation = "Sleep architecture loops in precise 90-minute periods. Waking at the tail-end of a cycle keeps you fully energized."
                )
            )
        ),
        Lesson(
            id = 33,
            title = "The Guilt-Free 'No'",
            category = "Growth",
            description = "Protect your personal time by politely turning down requests that do not align with goals.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Art of Boundary Setting",
                    content = "Saying yes to everything is a fast track to burnout. Reject requests that do not align with your core focus. Keep your decline brief, clear, and polite without making up complex, fake excuses.",
                    example = "Saying: 'Thanks for thinking of me! I'm completely booked with core projects right now, so I will have to pass.'",
                    tip = "Every time you say 'yes' to a distraction, you say 'no' to your high-level priorities."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the key to delivering a professional, guilt-free refusal?",
                    options = listOf("Generate a complex, fake excuse", "Keep it polite, direct, and brief without writing paragraphs", "Ignore their messages completely", "Offer to work late for free instead"),
                    correctAnswerIndex = 1,
                    explanation = "Keeping turn-downs brief and direct shows respect for your own schedule while remaining professional."
                )
            )
        ),

        // === SUCCESS CATEGORY (34 - 41) ===
        Lesson(
            id = 34,
            title = "OKR Goal Setting Framework",
            category = "Success",
            description = "Plan personal progress with measurable Objectives and Key Results.",
            xpReward = 100,
            badgeUnlocked = "OKR Strategist",
            slides = listOf(
                LessonSlide(
                    title = "What are OKRs?",
                    content = "Invented at Intel and scaled at Google, OKRs align goals simply. An Objective is WHAT you want to achieve (inspirational, ambitious). Key Results are HOW you measure it (quantitative, numeric, binary).",
                    example = "Objective: Build high personal stamina.\nKey Result: Run 5km under 24 minutes by October 1st.",
                    tip = "Limit himself to 3 Objectives per quarter with 3 Key Results each."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What characteristic is mandatory for a valid Key Result (KR) in the OKR framework?",
                    options = listOf("Broad description of activity", "Highly quantitative and measurable with numbers", "Easy enough to finish on day one", "Confidential from coworkers"),
                    correctAnswerIndex = 1,
                    explanation = "Key Results must be quantitatively measurable and clear, leaving zero debate about whether you hit the result."
                )
            )
        ),
        Lesson(
            id = 35,
            title = "The Eisenhower Matrix",
            category = "Success",
            description = "Sort daily targets into Urgent vs. Important quadrants to eliminate busywork.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Four Quadrants of Focus",
                    content = "This prioritization system sorts tasks into 4 cells: 1) Urgent & Important (Do now), 2) Important & Not Urgent (Schedule to build value), 3) Urgent & Not Important (Delegate), 4) Not Urgent & Not Important (Delete).",
                    example = "Setting up savings blocks falls in Quadrant 2. It isn't urgent, but holds high long-term value.",
                    tip = "High performers spend most of their time in Quadrant 2 to prevent crises before they happen."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "In which quadrant should high-performing professionals spend the majority of their strategy time?",
                    options = listOf("Urgent & Important (Q1)", "Important & Not Urgent (Q2)", "Urgent & Not Important (Q3)", "Not Urgent & Not Important (Q4)"),
                    correctAnswerIndex = 1,
                    explanation = "Spending time in Quadrant 2 allows you to build long-term systems and learn skills, preventing future emergencies."
                )
            )
        ),
        Lesson(
            id = 36,
            title = "Countering Imposter Syndrome",
            category = "Success",
            description = "Override career anxiety with objective artifact logs of your hard-earned wins.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Brag Folder Heuristic",
                    content = "Imposter syndrome is the psychological pattern of doubting your accomplishments and fearing exposure as a fraud. Override this bias by keeping a 'Brag Folder' containing screenshots of positive reviews, thank-you notes, and performance statistics.",
                    example = "Keep a Notion page of client praises to review whenever you feel career self-doubt.",
                    tip = "Look at these objective achievements as scientific evidence to dispute anxious thoughts."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary action in the 'Brag Folder' heuristic?",
                    options = listOf("Boasting to competitors", "Keeping a file of positive reviews and real results to counter doubt", "Hiding errors from managers", "Making up achievements on LinkedIn"),
                    correctAnswerIndex = 1,
                    explanation = "Reviewing objective praise files acts as a fast rational proof to calm emotional self-doubt."
                )
            )
        ),
        Lesson(
            id = 37,
            title = "Building Personal Leverage",
            category = "Success",
            description = "Stop selling hours. Harness code, media, and capital leverage to secure massive wealth.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Permissionless Leverage",
                    content = "Naval Ravikant divides leverage into: 1) Labor (hiring people), 2) Capital (money), 3) Products with zero marginal cost (Code and Media). Code and Media are standard, permissionless forces that work for you while you sleep.",
                    example = "Writing a script or publishing blogs. They cost zero to duplicate and expand to millions.",
                    tip = "Work on building assets that can run without your physical presence."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which type of leverage is permissionless and can scale to millions at zero marginal cost?",
                    options = listOf("Labor leverage", "Manual hours leverage", "Code and Media leverage", "Bank loans leverage"),
                    correctAnswerIndex = 2,
                    explanation = "Code and media work for you 24/7 without requiring anyone's permission to build, upload, or copy."
                )
            )
        ),
        Lesson(
            id = 38,
            title = "Billionaire Cold Outreach",
            category = "Success",
            description = "Craft concise, high-value pitches that capture any industry leader's attention.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The No-Fluff Cold Pitch",
                    content = "High-status leaders value time above everything. Never ask: 'Can I pick your brain?' Offer clear value, keep it to 3 sentences, and make it easy to reply with a simple, binary action call.",
                    example = "Bad: 'Love to chat about business.'\nGood: 'Built a simple tool showing 3 bugs on your landing page. Would Tuesday work for a quick look?'",
                    tip = "Do not include long introductions. Get straight to the value."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary mistake when cold-emailing busy industry leaders?",
                    options = listOf("Keeping it under 100 words", "Asking open-ended, time-consuming requests like 'picking their brain'", "Offering specific, actionable values", "Giving a brief yes/no action choice"),
                    correctAnswerIndex = 1,
                    explanation = "Open-ended, vague requests require high cognitive energy to reply to and are typically filtered or ignored."
                )
            )
        ),
        Lesson(
            id = 39,
            title = "The 5 AM Club Advantage",
            category = "Success",
            description = "Establish a quiet morning buffer routine to win the day before distraction stirs.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Silent Victory Buffer",
                    content = "Operating early provides a silent window free of emails, slacks, and family calls. Use this buffer for creative work or learning before active daily operations start. Capitalizing on this silent time builds momentum.",
                    example = "Waking up early to learn a coding language for 1 hour before starting standard work.",
                    tip = "Waking up early is only effective if you protect your sleep by going to bed early."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary advantage of waking early for personal study?",
                    options = listOf("It allows you to boast to friends", "It provides a quiet distraction-free window for intense study", "It speeds up system processes", "It allows you to skip breakfast"),
                    correctAnswerIndex = 1,
                    explanation = "Early morning presents high silence and zero inbound emails, allowing for highly sustained concentration."
                )
            )
        ),
        Lesson(
            id = 40,
            title = "Build a Public Brand Authority",
            category = "Success",
            description = "Become a trusted voice by publishing highly curated insights regularly on LinkedIn.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Documenting Your Professional Journey",
                    content = "Establish your brand without pretending to be a guru. Authentically document what you are actively learning, the bugs you find, and solutions you build. This highlights capability and attracts managers and recruiters organically.",
                    example = "Publishing: 'Just spent 3 hours resolving this database issue. Here's a 3-step solution to save you time.'",
                    tip = "Consistency is superior to perfect formatting. Aim to write 2 helpful posts weekly."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the most sustainable strategy for building a respectable personal brand?",
                    options = listOf("Pretend to be an expert in everything", "Share what you are learning and document your real journey", "Pay bots to inflate your follower count", "Spam links write reviews without context"),
                    correctAnswerIndex = 1,
                    explanation = "Documenting your real-world learnings showcases humility, active growth, and real functional ability."
                )
            )
        ),
        Lesson(
            id = 41,
            title = "Stoicism & Control Dichotomy",
            category = "Success",
            description = "Separate things you can control from details you cannot to achieve mental resilience.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Control Split",
                    content = "Stoicism focuses on the Dichotomy of Control. Divide situations into what you control (your efforts, choice, reactions) vs what you do not (market adjustments, other opinions, weather). Pour all energy into what you control.",
                    example = "You cannot control if a client accepts your bid, but you can control applying to 5 quality leads daily.",
                    tip = "When stress strikes, ask: 'Is this thing inside my actual control?' If no, let it go."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which of the following is inside your actual psychological control, according to Stoicism?",
                    options = listOf("Current market trends", "Other people's opinions", "Your choices and responses to events", "The weather forecast"),
                    correctAnswerIndex = 2,
                    explanation = "While external circumstances are uncontrollable, we are entirely in control of how we choose to interpret and react to them."
                )
            )
        ),

        // === SELF HELP CATEGORY (42 - 50) ===
        Lesson(
            id = 42,
            title = "Managing Upward in Careers",
            category = "Self Help",
            description = "Align professional results directly with your manager's highest priorities.",
            xpReward = 100,
            badgeUnlocked = "Sage Strategist",
            slides = listOf(
                LessonSlide(
                    title = "Solve Your Supervisor's Problems",
                    content = "The core of your career success is making your manager's life easier. Discover your supervisor's top priorities and shape your daily reports to directly highlight how you are resolving those key paint points.",
                    example = "Your manager hates compiling weekly spreadsheets. Take initiative and build an automated report.",
                    tip = "Never bring a problem to your manager without proposing 2 real solutions first."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is a core golden rule of managing upward?",
                    options = listOf("Complain about coworkers daily", "Never bring a problem to your manager without proposing solutions", "Agree to work late every single day", "Hide all errors and mistakes"),
                    correctAnswerIndex = 1,
                    explanation = "Proposing solutions displays initiative and high-level responsibility, immediately lessening your manager's workload."
                )
            )
        ),
        Lesson(
            id = 43,
            title = "Public Speaking Panic Hacks",
            category = "Self Help",
            description = "Use physiological adjustments to address performance anxiety and deliver flawless presentations.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Hacking the Adrenaline Surge",
                    content = "Stage anxiety is an adaptive adrenaline reaction. Override this bodily panic by practicing long exhalations (calming your heart rate) and choosing 'grounding anchors' like keeping feet flat or lightly touching a podium.",
                    example = "Take 3 deep abdominal breaths before stepping up to speak to calm your central nervous system.",
                    tip = "Anxiety and excitement are physically identical. Reframe your racing heart as 'I am excited!'"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "How can a speaker desensitize biological performance anxiety in 10 seconds?",
                    options = listOf("Drink a heavy espresso", "Sprint 100 meters", "Practice deep abdominal breaths with long exhalations", "Apologize to the crowd for being scared"),
                    correctAnswerIndex = 2,
                    explanation = "Shifting breathing rate down triggers deep calming signals directly inside brain fear centers."
                )
            )
        ),
        Lesson(
            id = 44,
            title = "Cognitive Reframing for Stress",
            category = "Self Help",
            description = "Challenge negative assumptions with factual evidence-based self-dialogue.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Disputing Catastrophizing",
                    content = "Our brain makes up dramatic threats during stress (catastrophizing). Break this anxious cycle of automated thoughts by questioning them: 'Is this thought undeniably true, or is this just an anxiety interpretation?' Prove your thoughts wrong.",
                    example = "Anxious thought: 'I missed a typo, I will get fired.' Reframe: 'Everyone makes typos. My boss valued my content.'" ,
                    tip = "Look for factual evidence of your capability to counter negative assumptions."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the core step in practicing Cognitive Reframing?",
                    options = listOf("Ignoring all problems and smiling", "Challenging negative thoughts with factual evidence", "Complaining to coworkers", "Stopping focused work"),
                    correctAnswerIndex = 1,
                    explanation = "Reframing shifts emotional catastrophizing to objective reality, eliminating useless stress cycles."
                )
            )
        ),
        Lesson(
            id = 45,
            title = "The 4-7-8 Deep Breathing",
            category = "Self Help",
            description = "An ancient tactical breathing system to calm your physical nervous threat response.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Dr. Andrew Weil's Relaxation Heuristic",
                    content = "Calm your nervous system using Dr. Andrew Weil's 4-7-8 method: inhale quietly through your nose for 4 seconds, hold your breath for 7 seconds, and exhale completely making a whoosh sound for 8 seconds. This resets your nervous system.",
                    example = "Use this breathing pattern right before entering high-stakes business meetings or exams.",
                    tip = "Practice the loop exactly 4 times in sequence for deep physical calming."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "For how many seconds should you hold your breath in the 4-7-8 method?",
                    options = listOf("4 Seconds", "7 Seconds", "8 Seconds", "12 Seconds"),
                    correctAnswerIndex = 1,
                    explanation = "You inhale for 4 seconds, hold for 7 seconds, and exhale for 8 seconds to reset your nervous system."
                )
            )
        ),
        Lesson(
            id = 46,
            title = "Contentment Gratitude Logs",
            category = "Self Help",
            description = "Re-wire baseline emotional states by logging specific daily micro-positive events.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Fighting Hedonic Adaptation",
                    content = "Our brain naturally focuses on issues to keep us safe (negativity bias). Override this cycle by logging exactly 3 specific, tiny things you are grateful for daily. Specificity builds positive neural pathways much faster.",
                    example = "Bad: 'Nice day.'\nGood: 'Grateful for that delicious hazelnut coffee from cafe on corner.'",
                    tip = "Write notes down by hand or in log to reinforce positive connections."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What makes gratitude logging highly effective for building long-term focus and contentment?",
                    options = listOf("It makes you very rich", "It forces your mind's focus off negativity bias", "It replaces standard workout habits", "It saves device energy"),
                    correctAnswerIndex = 1,
                    explanation = "Intentionally logging micro-positives counters natural survival negativity, improving focus and mood."
                )
            )
        ),
        Lesson(
            id = 47,
            title = "Toxic Relationship Boundaries",
            category = "Self Help",
            description = "Formulate crisp, polite boundaries with draining colleagues, friends, or family.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Polite Steel Wall",
                    content = "Setting boundaries means outlining what behaviors you will accept from others. State your conditions in a direct, polite, non-defensive manner. Focus entirely on your boundaries rather than launching personal attacks.",
                    example = "Saying: 'I can only talk during office hours. If you email on Sunday, I will reply on Monday.'",
                    tip = "A boundary without consequences is just a suggestion."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the key to maintaining a healthy boundary?",
                    options = listOf("Angry verbal fights", "Defending yourself with complex stories", "Politely stating conditions and enforcing them with clear actions", "Avoiding all contact permanently"),
                    correctAnswerIndex = 2,
                    explanation = "Stating boundaries calmly and defending them with actions forces professional respect without escalating arguments."
                )
            )
        ),
        Lesson(
            id = 48,
            title = "Beat Procrastination: 5s Rule",
            category = "Self Help",
            description = "An instant cognitive trick to kickstart friction-heavy actions before overthinking starts.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The 5-4-3-2-1 Blast Off Method",
                    content = "Popularized by Mel Robbins, the 5-Second Rule is a simple heuristic: when you feel hesitation before a task, count down 5-4-3-2-1 out loud and physically start moving. This prevents your brain from complicating or delaying the action.",
                    example = "Counting down 5-4-3-2-1 and opening your book instead of scrolling on social media.",
                    tip = "Act immediately on the count of 1. Do not give your brain time to form excuses."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Why does Mel Robbins' 5-Second Rule successfully bypass procrastination?",
                    options = listOf("It uses computational math", "It interrupts the brain's natural anxiety hesitation delay", "It completely deletes the target task", "It notifies friends automatically"),
                    correctAnswerIndex = 1,
                    explanation = "Counting down from 5 keeps your mind focused on numbers, blocking anxious loops so you can act easily."
                )
            )
        ),
        Lesson(
            id = 49,
            title = "Therapeutic Writing Method",
            category = "Self Help",
            description = "Utilize free-association journaling to decrypt and clear mental fog or sadness.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "The Free-Flow Dump",
                    content = "Unexpressed thoughts build emotional pressure. Free-association journaling involves writing continuously for 10-15 minutes without editing, correcting spelling, or filtering. This untangles messy feelings from logic.",
                    example = "Writing down whatever comes to mind, even if it is: 'I do not know what to write.'",
                    tip = "Destroy or password-protect your files afterwards so you can write without any worry."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary rule of free-association therapeutic journaling?",
                    options = listOf("Ensure perfect grammar and structure", "Write continuously without filtering or editing thoughts", "Show the logs to colleagues", "Limit yourself to three words"),
                    correctAnswerIndex = 1,
                    explanation = "Writing without filtering lets your subconscious express deep worries, revealing the causes of underlying stress."
                )
            )
        ),
        Lesson(
            id = 50,
            title = "The Cure for Perfectionism",
            category = "Self Help",
            description = "Ditch flawless expectations; move to highly feedback-driven, rapid output habits.",
            xpReward = 100,
            slides = listOf(
                LessonSlide(
                    title = "Done is Better than Perfect",
                    content = "Perfectionism is a fear of evaluation disguised as premium standards. It results in slow progress and major procrastination. Shift your mindset to: 'Aim for 80% quality, ship rapidly, and improve using real-use feedback.'",
                    example = "Publishing a simple app with core functions, rather than spending 2 years perfecting features.",
                    tip = "Remember, a shipped product in active use is infinitely more instructional than a perfect draft in a folder."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "What is the primary danger of practicing unhealthy perfectionism?",
                    options = listOf("Getting lots of positive reviews", "High costs in electricity", "Severe procrastination and slow project progress", "Developing high self-esteem"),
                    correctAnswerIndex = 2,
                    explanation = "Fearing negative reviews causes perfectionists to delay shipping, blocking learning and feedback opportunities."
                )
            )
        )
    )

    val roadmapsList = listOf(
        Roadmap(
            id = "excel_10",
            title = "Learn Excel Mastery in 5 Days",
            description = "Get spreadsheet literate and learn shortcuts to automate corporate tasks super quickly.",
            category = "Productivity",
            durationDays = 5,
            badgeReward = "Excel Legend",
            isPremium = false,
            tasks = listOf(
                RoadmapTask(1, "Formulas: VLOOKUP & XLOOKUP", "Learn to cross-reference data sources correctly.", 1),
                RoadmapTask(2, "Keyboard Hacks & Navigation", "Navigate complex sheets without touching the mouse (Alt shortcuts)."),
                RoadmapTask(3, "Pivot Tables & Graphs", "Summarize 10,000 rows of sales data into clean visual charts."),
                RoadmapTask(4, "Data Cleaning: Text-to-Columns", "Extract raw formats, remove duplicates, and structure reports."),
                RoadmapTask(5, "Excel Macros & Basic Automation", "Record your first macro to automate daily morning reports.")
            )
        ),
        Roadmap(
            id = "career_ready",
            title = "Job Prep Career Booster",
            description = "A rapid daily checklist to polish your online presence, resume, and communication skills.",
            category = "Career Growth",
            durationDays = 5,
            badgeReward = "Market-Ready Pro",
            isPremium = false,
            tasks = listOf(
                RoadmapTask(1, "Resume Keyword Match & ATS", "Restructure your resume to bypass ATS scanner bots.", 6),
                RoadmapTask(2, "Elevator Pitch Practice", "Record a 60-second audio summary introducing your project.", 4),
                RoadmapTask(3, "Craft Your Linkedin Profile", "Set a custom header, write a 3-sentence summary, and optimize job descriptions."),
                RoadmapTask(4, "Master Behavioral Interview Questions", "Learn the STAR format: Situation, Task, Action, Result."),
                RoadmapTask(5, "Reach Out & Network", "Build templates for cold messaging recruiters on LinkedIn.")
            )
        ),
        Roadmap(
            id = "freelance_30",
            title = "Freelancer Starter Blueprint",
            description = "Step-by-step instructions to list your skills, secure international clients, and price your projects.",
            category = "Career Growth",
            durationDays = 5,
            badgeReward = "Freelance Hustler",
            isPremium = true,
            tasks = listOf(
                RoadmapTask(1, "Positioning Your Skillset", "Find a high-income niche like tech translation, Shopify dev, or video editing."),
                RoadmapTask(2, "Build an Elite Portfolio", "Create 3 spec work projects to prove your mastery to prospective clients."),
                RoadmapTask(3, "Optimizing Upwork & Fiverr Profiles", "Write click-worthy headlines and film a 30s greeting intro video."),
                RoadmapTask(4, "Writing Proposals that Convert", "Analyze potential client posts & draft bespoke client value letters."),
                RoadmapTask(5, "Client Retention & Retention Secrets", "Charge fixed retainer rates instead of hourly to double earnings.")
            )
        ),
        Roadmap(
            id = "startup_launcher",
            title = "Startup Founder Launchpad",
            description = "Validate bold tech ideas, build high-converting MVPs, deconstruct competitor products, and achieve Product-Market Fit.",
            category = "Business",
            durationDays = 5,
            badgeReward = "Startup Architect",
            isPremium = false,
            tasks = listOf(
                RoadmapTask(1, "Mind of the Lean Founder", "Deconstruct risks and understand the minimum viable product (MVP) model.", 7),
                RoadmapTask(2, "Deconstruct Competitors", "Ethics-driven templates to map rival offerings and find Blue Ocean market gaps.", 12),
                RoadmapTask(3, "Synthesize Product-Market Fit", "Measure real demand using the Sean Ellis 40% feedback test parameters.", 9),
                RoadmapTask(4, "Elite B2B Cold Outreach", "Construct a 3-step value-loaded message to acquire premium business customers.", 8),
                RoadmapTask(5, "Scale with Unit Economics", "Align marketing acquisition costs (CAC) with total customer lifetime value (LTV).", 11)
            )
        ),
        Roadmap(
            id = "hyper_learner",
            title = "Hyper-Learning & Mental Models",
            description = "Ditch old rote memorization. Harness visual mind maps, active spacing models, and Feynman analogies to learn 3x faster.",
            category = "Learning",
            durationDays = 5,
            badgeReward = "Cognitive Mastermind",
            isPremium = false,
            tasks = listOf(
                RoadmapTask(1, "The Richard Feynman Paradigm", "Break complex theories into extremely simple child-level explanations.", 16),
                RoadmapTask(2, "Active Recall & Spaced Practice", "Challenge active neural pathways right before memory decay occurs.", 17),
                RoadmapTask(3, "The 80/20 Pareto Filter", "Ditch unnecessary topics. Identify the 20% highest impact knowledge assets.", 18),
                RoadmapTask(4, "Visual Association Mapping", "Design branching mind maps that mirror natural brain connections.", 24),
                RoadmapTask(5, "Ultra Speed Reading Speed", "Mute the slow sub-vocal inner voice to double parsing speeds instantly.", 19)
            )
        ),
        Roadmap(
            id = "peak_performance",
            title = "Atomic Habits & Deep Focus",
            description = "Master your daily workflows. Stack compounding habits, trigger deep focus states, and biohack dopamine levels.",
            category = "Growth",
            durationDays = 5,
            badgeReward = "Growth Sovereign",
            isPremium = false,
            tasks = listOf(
                RoadmapTask(1, "Atomic Habit Stacking", "Form reliable routines easily by anchoring behaviors to current automatic habits.", 26),
                RoadmapTask(2, "Kaizen: 1% Continuous Gains", "Understand the mathematical power of compounding 1% tiny progress daily.", 28),
                RoadmapTask(3, "Distraction-Free Deep Work", "Block intense 90-minute study sessions with zero notifications or phone pings.", 29),
                RoadmapTask(4, "Voluntary Discomfort Training", "De-scare the amygdala with small, safe discomfort friction drills.", 30),
                RoadmapTask(5, "Protecting Your Willpower Reserve", "Schedule primary focus exercises when decision willpower is fully charged.", 31)
            )
        ),
        Roadmap(
            id = "wealth_architecture",
            title = "Wealth & Elite Negotiation",
            description = "Master your financial streams. Build compounding setups, price your value correctly, and negotiate high-tier deals.",
            category = "Success",
            durationDays = 5,
            badgeReward = "Wealth Vanguard",
            isPremium = true,
            tasks = listOf(
                RoadmapTask(1, "Personal Finance: 50/30/20 Rule", "Budget systematically using the modern Elizabeth Warren allocation.", 5),
                RoadmapTask(2, "Outcomes value Pricing", "Ditch boring, low hourly rates and align fees directly with deliverable outcomes.", 10),
                RoadmapTask(3, "High-Ticket Anchor Deals", "Offer package tiers to shift client decision from Yes/No to Which Package.", 15),
                RoadmapTask(4, "Balance Sheets Simplified", "Deconstruct corporate reports down to Revenue, OpEx, and EBITDA quickly.", 14),
                RoadmapTask(5, "Exponential Compound Interest", "Uncover how the mathematical force of early investing grows passive assets."),
            )
        ),
        Roadmap(
            id = "emotional_genius",
            title = "Improvisation & Confidence Boost",
            description = "Overcome personal self-doubt. Build an unshakeable mindset, beat dopamine burnout, and command social environments.",
            category = "Self Help",
            durationDays = 5,
            badgeReward = "Unshakeable Aura",
            isPremium = true,
            tasks = listOf(
                RoadmapTask(1, "Growth Mindset Evolution", "Adopt the ultimate psychologist Carol Dweck grit standard for challenges.", 25),
                RoadmapTask(2, "Dopamine Detox & Re-sensitize", "Flush continuous online scrolls to re-learn relaxed cognitive focus.", 27),
                RoadmapTask(3, "Micro Social Courage drills", "Learn to speak boldly from the gut under elevator-pitch pressure.", 4),
                RoadmapTask(4, "Sleep Hygiene & Brain Health", "Synchronize sleep patterns around 90-minute REM intervals, waking fresh.", 32),
                RoadmapTask(5, "Defeating Imposter Syndrome", "Build a visual brag-file of absolute wins to shut down subconscious anxiety.")
            )
        )
    )
}

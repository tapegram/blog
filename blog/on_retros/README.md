# On Retros

## Intro
I can't think of a more pretentious name for a blog post than this. It's honestly kind of embarassing. Actually, writing any thoughts online is embarassing but it's a helpful exercise (I hope!).

Anyway, this is another opinion and personal experience heavy post about one of my favorite topics: Sprint Retrospectives. The longer I operate as a self-styled "software engineer," the more and more I am convinced that the structure and processes of an organization and the teams that write the software are inextricably tied to the resulting code and success of the product. There is a long-winded and rambling talk I've given attempting to appraise this at a high level [on youtube](https://www.youtube.com/watch?v=nEGIfvxK8Uo) in case you have entirely too much free time. 

That being said, if I can convince you of nothing else over this post, I'd like to convince you that retros are the best tool to improve your team's process and structure in an agile, incremental, bottom-up way -- and as a result, improving the quality and quantity of your code and delivering more value for your organization.

I'm also pretty well convinced that the best way to deliver better value for your organization is to "grow" your engineers (from juniors to seniors) and your teams (from a collection of engineers to a real, stable, [TEAM](https://www.youtube.com/watch?v=pGFGD5pj03M&ab_channel=Channel4)).

This post takes these ideas/values as a given. If you want to hear more here are some resources I would recommend:
1) [XP Explained](https://www.amazon.com/Extreme-Programming-Explained-Embrace-Change-ebook/dp/B00N1ZN6C0/ref=sr_1_3?crid=ZKUNAKHQO1EF&keywords=xp+explained&qid=1644086745&sprefix=xp+explain%2Caps%2C364&sr=8-3)
2) [Object Thinking](https://www.amazon.com/Object-Thinking-Developer-Reference-David/dp/0735619654/ref=sr_1_1?crid=2DBJV2RFM1TDY&keywords=object+thinking&qid=1644086785&sprefix=object+thinking%2Caps%2C94&sr=8-1)
3) [Teaching Functional Programming to Noobs](https://www.youtube.com/watch?v=bmFKEewRRQg)

I also want to mention that teams (like individuals) need to build competency in nearly everything they do. Newer or more unstable teams, regardless of the individual talent they have, will not always succeed in applying good ideas / processes / patterns. Part of retros is focused on building competencies with processes as crutches, until the process is no longer needed and doing away with it. My fencing coach once told me:
> If you are losing it is because you can't tell what the opponent is doing, 
> 
> or you know what they are doing but you don't know what to do about it
> 
> or you know what they are doing and you know what to do about it, but your don't have the technical ability to execute on it.

This is the exact kind of "Art of War" wisdom that I love because in some ways it's like "well, duh" but also I find the ideal so well articulated that I keep coming back to it and applying it as a problem-solving framework.

In software terms this might be
1) Have you identified the REAL problem?
2) Have you identified the REAL solution (or at least a good solution)?
3) Have you actually executed the solution well?

Any of these can fail, and its crucial to be able to identify what step you are failing on. Too often I see teams do 1 & 2 correctly, but lack the experience as a team to execute on the solution. They then abandon the solution entirely instead of working to build the competency needed to execute it.

Being at least "pretty good" at this kind of problem-solving is essential to running successful retros.

Here is a rough outline of what I'd like to cover:
1) Why retros are so important
2) What makes a retro "good" and what makes one "bad"
3) Some success stories
4) A collection of common problems I've seen teams encounter and some possible retro driven solutions (read: patterns!).

And finally, as a massive disclaimer, these are all ideas and recommendations from my own personal experience. What works for me may not work for you. I'm certain that in another year/month/tomorrow, I will learn something new and change all my recommendations here. Feel free to take and try anything you like but just remember that there is no "one true way" and agile/retros are all about learning and trying new things.

## Why Retros?
[Webster's Dictionary defines](https://www.youtube.com/watch?v=s93EZfNvOyw&ab_channel=SCProductions) retros as

>a meeting to look back over an iteration, release, or project, specifically to discuss what worked well, what could be improved, and most importantly, how to translate the lessons learned into actionable change. Retrospectives are a forum for the team to improve upon their process. They’re an integral part of Scrum and Extreme Programming (XP).
>
>Retrospectives directly address one of the twelve principles of Agile: At regular intervals, the team reflects on how to become more effective, then tunes and adjusts its behavior accordingly.
>
>This can result in: improved productivity, better capability, higher product quality, and happier team members.

[*actual source](https://www.intelliware.com/agile-retrospectives/)

It sounds innocuous, but it's actually "the most important" thing you can do to improve your teams. If I had to pick one single process to bring to any team, its retros. Everything else is emergent from retros.

Retros matter because:
1) they provide a simple framework to continuously and iteratively improve you team, your work, and ideally, your life.
2) when you improve your own life, you tend to provide better outcomes for the business (happy engineers => better outcomes, less turnover, easier recruiting, more learning, etc.)
3) they provide a venue for experimentation which can lead to radical changes over time

### Make your own life better!
1) At its most basic, the job of a software engineer is to deliver "value"
2) We have learned that the best way to deliver value is to be "agile" (except in rare circumstances)
3) Being "agile" requires building a system (team and code) that is easy to change: "Embrace Change"
4) A system that is easy to change is the best possible situation for an engineer to be in day to day. In fact, nearly all "engineering" is based around making it easy to change things: think PORTS AND ADAPTERS and TDD and SOLID and basically everything you have ever learned.
5) If you invest in "making your own life better" you are really applying agile principles and good engineering to make a reactive, generic, amazing platform/product.
6) So your job indirectly to make your own life better -- and value for the business ends up being a byproduct of that.

## What Makes a Retro "Good" and What Makes One "Bad"
Well everyone probably already knew what a retro is by the definition but what are some more concrete details? How does it work?

### Here is what I like to see in a retro
1) Engagement from everyone on the team.

All levels of seniority and all functional roles should be contributing. It's not "bottom up" if only some specific subset of "enlightened" and confident (read: tenured) engineers are contributing. You must build a culture that encourages team members to share their experiences and ideas during retros, otherwise nothing will change.

2) Should include both "problems" and "successes."

When identifying problems or areas of improvement, it's always good to ask why [and then maybe why another 4 times.](https://en.wikipedia.org/wiki/Five_whys). What can we do to mitigate or solve the problem next sprint? Can we change a process? Can we automate something? Can we change the way we approach the problem?

But fixing problems is only half the value of the retro. You also should discuss successes, celebrate wins, and look to push the envelope! XP, after all, is founded on the idea of "turning up the good" and "turning the dials up to 11." If code reviews are good, what would be better? Pair programming! If pair programming is good, what would be better? Mob programming? Work out the details over several sprints and retros.

3) Should be a venue for experimentation.

Because you are already working in increments of time (I prefer 1 week sprints since that is the smallest increment my teams have been able to deliver value in while minimizing the latency on our "feedback loops" but do whatever your team is comfortable with) it's relatively low risk to try new things. Worst case scenario is you lose one sprint. So brainstorm ideas, try them, and review them next retro. Then iterate again!

4) Should result in small, well-defined action items.

Like engineering sprint work, the smaller and more well-defined the action item the more likely it will actually get done. Action items that basically amount to "try not to have as much rollover" probably won't be nearly as effective as "let's break down all 5+ point tickets into smaller tickets before bringing them into the sprint" or "let's configure jira to allow a maximum number of 4 in-progress tickets at a time."

5) The primary goal should be to build the team that the member's want to work on. Iteratively turn your job into your [dream job!](https://www.youtube.com/watch?v=gAMu5YMOZn0&ab_channel=DivaDan)

Every engineering team I have ever worked on has been full of smart capable people who love to write code and want to get work done, but they have problems getting in their way. Too many meetings, too much rollover, confusion on what the user story is asking for, feeling isolated from team members, etc. If it's bothering you or getting in your way, bring it to retro and try to get it fixed! If you think there is a "better way" there is no reason we shouldn't shoot for it.

### Here are common problems I see in running retros

More detailed patterns will be included later.

#### Lack of engagement
I've heard people say retros are "a waste of time", "nothing ever improves," "no one talks," "it's boring," "no one pays attention," etc.

Agile's biggest flaw, like with OOP and many other Good Things, is that it is easy to do it badly, think it's terrible, and throw it away.

If the team doesn't have faith in the retro process, they won't engage with it, and it becomes a self-fulfilling prophecy. And there can be a variety of reasons for this
1) Internalization that work is "unpleasant" and always will be
2) Learned helplessness
3) Feeling like one's experiences or ideas are not valuable enough to be shared.

These are hard problems to solve, but here are some of my recommendations
1) Evangelize the process. Talk up how the point of the retro is to make everyone happier at work.
2) Scope down the action items and track them to make sure they actually get done. Show the team that real change comes from retros.
3) Make space for people to share their experiences and engage with them. Don't just dismiss them out of hand. Sometimes it's good just to try stuff people suggest even if you think its silly, because you can always revisit it next retro and now you have convinced team members that their thoughts matter and can cause change.

#### Action Items are not Done
If you take an hour every week to earnestly discuss changes, write up and assign out action items for the next sprint, and then never do them, the team will quickly learn that retro is a waste of time.

So do what it takes to make small actionable tasks and make sure they get done. I have some suggestions for this further down.

#### Conversations Run Long and Time Runs Out
Sometimes there is bikeshedding or sometimes the team gets into a really good conversation about a specific topic. It's hard to know when to stop. Over time, the team will need to learn to keep itself on track. Until then, the facilitator will need to refocus the team when the discussion gets too off the rails. If it's a good discussion but isn't going to result in a decision any time soon, take an action item to schedule dedicated time to discuss that issue and move on. Give the big topics the time they deserve, don't limit them to your retro time block.

#### Toxic Professionalism
I've found that many people have internalized "professionalism" to mean not complaining, sucking it up, and pushing through problems. Quietly suffering is a recipe for disaster. Fail loudly -- always. Shorten the iterations you work in to make sure you can safely fail early and often.

Complaints are good! That's how we know what to improve! "The squeeky wheel gets the grease."

Just make sure you complain constructively and in pursuit of a solution. That means not blaming individuals for their mistakes or other issues. I would again suggest keeping in mind the "5 whys." Nearly all problems are institutional and structural, which is what retros try to solve (at whatever level the "team" exists at).

All of your colleagues are smart and well-intentioned people struggling to do the right thing within the context of organizations that often discourage it. If it well and truly is a problem with in an individual, that's a conversation to have with their manager (or a recruiter at another company).


## Some Examples of Retro-Driven Successes

### Tracking Action Items in the Sprint
We noticed that action items were often not done or people were scrambling to do them right before the next retro.

We thought about why this happened, and landed on that it's a whole other thing people have to remember to do, it’s not really visibly tracked, and they aren’t part of the engineers' daily work.

So we started putting all action items in 0 point tickets at the top of the next sprint. Now they are super visible and way more of them get done. In fact, on the team I'm currently on, we have done nearly every action item from every retro in the following sprint (knock on wood). I think we had one rollover item one time, and it was addressed in the next sprint after it was brought up in retro again.

And to push it further, we started tracking retro action item tickets in JIRA with a specific label so we can look at a dashboard of jira metrics at the end of each sprint (along with other cool data like interrupts, time to close tickets, etc.). I'm very excited about how this level of insight might help us make data driven process changes in the future!

### Pairing and Mob Programming
One retro years ago we were discussing issues involving long delays for reviewing PRs. At the time, it was our biggest bottleneck. Someone casual threw out the idea of "pair programming being a continuous code review" with a built-in reviewer, so that retro we got sponsorship from a manager and decided to have one person on the squad exclusively pair for their work (pulling in different team members for each PR).

Each retro we checked in on how everyone was feeling about pairing and about the PR review turnaround, and people were increasingly liking the results of pairing. More and more people started doing it as their default method of work, and we continued to discuss the logistics of it during retros.

Eventually, it became everyone's favorite way to work and we started experimenting with mob programming (to address other issues involving siloing of knowledge about different features).

Now mob and pair programming are basically the default ways we work, and our retro discussion about them are more focused on 1) how to maximize the value and efficiency of mobs 2) how to determine when it makes the most sense to swarm vs mob, or just to go heads down by yourself on a problem.

### Fullstack teams
This is very close to the same story as pair and mob programming, except we took a similar incremental and experimental approach to transitioning our separate "front end" and "back end" teams into cross-functional fullstack teams.

One of the original problems we were trying to solve was just the communication barrier and latency of delivering value if we had to prioritize work for a single feature across multiple different teams, and all the standard functionally segregated team problems you have probably experienced.

We put one backend engineer on a frontend team to start. We continued to point frontend and backend tickets separately, and we increasingly leveraged pair programming between frontend and backend engineers.

This went well and we increasingly staffed frontend and backend engineers to the same teams.

Still going well, we pushed hard on pairing to build expertise in our existing engineers so people were more comfortable working full stack as individuals.

And eventually we decided to start writing our stories as single full stack vertical slices.

And now we are continuing to apply the same concepts with SDETs, SREs, and designers to iteratively build out true cross-functional teams.

### Work-Life Balance
COVID and the transition to remote work has been a big challenge for us. Problems were coming up in retro about how work was going later and later because the "return commute" hard stop at the end of the day disappeared. People found they were working more than when we worked in the office, but also were communicating and socializing less and were starting to burn out.

So we tried a few things.
1) We scheduled a recurring monthly in person happy hour (once everyone was vaccinated) so people had the option of interacting in person with their coworkers.
2) We started experimenting with recurring zoom "game nights." We did a few ad hoc starting at 5pm (so the day actually ended at 5) and if you wanted to you could join in to play code names, or among us, or jackbox games. Depending on how the team felt week to week, we would start to have these as often as every day.
3) We gave everyone a personal action item to go ahead and schedule a bunch of PTO. In our experience, unlimited vacation days leads to people not taking vacations. So we tracked setting up vacation days in our sprints for a while.
4) We also experimented with other mini end of day events like a "mini retro" every day where teams could demo their work for the day and get change management and UAT signoff and bring up issues we could discuss in standup the next morning.

## Patterns!

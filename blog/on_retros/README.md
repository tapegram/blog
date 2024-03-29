# On Retros

## Intro
I can't think of a more pretentious name for a blog post than this. It's honestly kind of embarrassing. Actually, writing any thoughts online is embarrassing but it's a helpful exercise (I hope!).

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
4) A system that is easy to change is the best possible situation for an engineer to be in day to day. In fact, nearly all "engineering" is based around making it easy to change things: think [PORTS AND ADAPTERS](../patterns/ports_and_adapters.md) and [TDD](../patterns/test_driven_development.md) and SOLID and basically everything you have ever learned.
5) If you invest in "making your own life better" you are really applying agile principles and good engineering to make a reactive, generic, amazing platform/product.
6) So your job indirectly to make your own life better -- and value for the business ends up being a byproduct of that.

Phrased another way, using "make my own working experience better" as a guiding principle converges, in practice, with the values and principles of Agile. As @pbouzakis mentioned in a comment on a draft of this blog post, this may deserve a deeper dive into ideas about [employees motivations](https://www.mindtools.com/pages/article/newLDR_74.htm). In my experience, maybe due to how we have done hiring, I have found all of my coworkers to be highly motivated to do good work and grow as engineers.

Of course, there are many people who, understandably, want to keep their work and their "life" separate. This is also great! I don't want it to sound like I'm advocating for your work becoming your life. But even if you want to fully compartmentalize your work and home lives, improving your work experience, through retros, will make those 40 hours a week more pleasant. And I don't know about you all but no matter how much I try to compartmentalize, having a bad time at work affects me at home too (and the other way around).

## What Makes a Retro "Good" and What Makes One "Bad"
Well everyone probably already knew what a retro is by the definition but what are some more concrete details? How does it work?

### Here is what I like to see in a retro
1) Engagement from everyone on the team.

All levels of seniority and all functional roles should be contributing. It's not "bottom up" if only some specific subset of "enlightened" and confident (read: tenured) engineers are contributing. You must build a culture that encourages team members to share their experiences and ideas during retros, otherwise nothing will change.

This could open up the question of "what is the team?" I don't have a definite answer to that, but I would suggest inviting anyone who works closely with you to participate. They are always free to decline! I don't have a lot of experience with this, but I would be very interested in seeing how retros with some "special guests" would go, like maybe directors or even executives. Stakeholders and "onsite customers" might provide another dimension of ideas for things to try, especially if you want to work beyond [Domain Driven Design and practice "Domain Design."](https://youtu.be/XH_awPS6hK4?t=1227)

2) Should include both "problems" and "successes."

When identifying problems or areas of improvement, it's always good to ask why [and then maybe why another 4 times.](https://en.wikipedia.org/wiki/Five_whys). What can we do to mitigate or solve the problem next sprint? Can we change a process? Can we automate something? Can we change the way we approach the problem?

But fixing problems is only half the value of the retro. You also should discuss successes, celebrate wins, and look to push the envelope! XP, after all, is founded on the idea of "turning up the good" and "turning the dials up to 11." If code reviews are good, what would be better? Pair programming! If pair programming is good, what would be better? Mob programming? Work out the details over several sprints and retros.

3) Should be a venue for experimentation.

Because you are already working in increments of time (I prefer 1-week sprints since that is the smallest increment my teams have been able to deliver value in while minimizing the latency on our "feedback loops" but do whatever your team is comfortable with) it's relatively low risk trying new things. Worst case scenario is you lose one sprint. So brainstorm ideas, try them, and review them next retro. Then iterate again!

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
1) Evangelize the process. Talk up how the point of the retro is to make everyone happier at work. I mention retros at basically every standup and pair/mob session whenever any kind of idea or problem comes up. "Oh that sounds like something we should fix, let's talk about that at retro." "Oh, I don't like that you have had to spend so much time working on this frustrating process, let's talk about it at retro and figure out how we can make it easier for you."
2) Scope down the action items and track them to make sure they actually get done. Show the team that real change comes from retros.
3) Make space for people to share their experiences and engage with them. Don't just dismiss them out of hand. Sometimes it's good just to try stuff people suggest even if you think its silly, because you can always revisit it next retro and now you have convinced team members that their thoughts matter and can cause change.

These efforts may need to start with you and any alies you can find who are similarly bought into retros. But eventually, these should become "team" responsibilities. Teams should be self-organizing and engineering led. This includes running the processes and ceremonies. Through retros, the team should refine how they collectively run the retro process and how they do things like creating refined action items.

#### Action Items are not Done
If you take an hour every week to earnestly discuss changes, write up and assign out action items for the next sprint, and then never do them, the team will quickly learn that retro is a waste of time.

So do what it takes to make small actionable tasks and make sure they get done. I have some suggestions for this further down.

#### Conversations Run Long and Time Runs Out
Sometimes there is bikeshedding or sometimes the team gets into an excellent conversation about a specific topic. It's hard to know when to stop. Over time, the team will need to learn to keep itself on track. Until then, the facilitator will need to refocus the team when the discussion gets too off the rails. If it's a good discussion but isn't going to result in a decision any time soon, take an action item to schedule dedicated time to discuss that issue and move on. Give the big topics the time they deserve, don't limit them to your retro time block.

#### Toxic Professionalism
I've found that many people have internalized "professionalism" to mean not complaining, sucking it up, and pushing through problems. Quietly suffering is a recipe for disaster. Fail loudly -- always. Shorten the iterations you work in to make sure you can safely fail early and often.

Complaints are good! That's how we know what to improve! "The squeaky wheel gets the grease."

Just make sure you complain constructively and in pursuit of a solution. That means not blaming individuals for their mistakes or other issues. I would again suggest keeping in mind the "5 whys." Nearly all problems are institutional and structural, which is what retros try to solve (at whatever level the "team" exists at).

All of your colleagues are smart and well-intentioned people struggling to do the right thing within the context of organizations that often discourage it. If it well and truly is a problem with in an individual, that's a conversation to have with their manager (or a recruiter at another company).

#### Retro isn't fun
It doesn't have to be the greatest thing in the world, but retro is better if people aren't dreading it. Good thing retro is a perfectly good subject for retros!

I don't know if this is the example anyone would want to copy, but one team I was on renamed retros to "Sprint Celebration," the facilitator was required to make (and expense) cocktails for the team (I always did Whiskey Sours), and then at the end we would nominate each other for sprint MVP and hand out an enormous novelty trophy. We had to move this meeting to 4pm on Fridays for obvious reasons. As a result it tended to accidentally segue into a full-blown company happy hour. A lot of people tried to join that team.

![Joanne and Paul, Team MVPs!](miso_trophy_winners.jpeg?raw=true "Joanne and Paul, Team MVPs!")


## Some Examples of Retro-Driven Successes

### Tracking Action Items in the Sprint
We noticed that action items were often not done or people were scrambling to do them right before the next retro.

We thought about why this happened, and landed on that it's a whole other thing people have to remember to do, it’s not really visibly tracked, and they aren’t part of the engineers' daily work.

So we started putting all action items in 0 point tickets at the top of the next sprint. Now they are super visible and way more of them get done. In fact, on the team I'm currently on, we have done nearly every action item from every retro in the following sprint (knock on wood). I think we had one rollover item one time, and it was addressed in the next sprint after it was brought up in retro again.

And to push it further, we started tracking retro action item tickets in JIRA with a specific label so we can look at a dashboard of jira metrics at the end of each sprint (along with other cool data like interrupts, time to close tickets, etc.). I'm very excited about how this level of insight might help us make data driven process changes in the future!

### Pairing and Mob Programming
One retro years ago we were discussing issues involving long delays for reviewing PRs. At the time, it was our biggest bottleneck. Someone casual threw out the idea of "pair programming being a continuous code review" with a built-in reviewer, so that retro we got sponsorship from a manager and decided to have one person on the squad exclusively pair for their work (pulling in different team members for each PR).

Each retro we checked in on how everyone was feeling about pairing and about the PR review turnaround, and people were increasingly liking the results of pairing. More and more people started doing it as their default method of work, and we continued to discuss the logistics of it during retros.

Eventually, it became everyone's favorite way to work, and we started experimenting with mob programming (to address other issues involving siloing of knowledge about different features).

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
4) We also experimented with other mini end of day events like a "mini retro" every day when teams could demo their work for the day and get change management and UAT signoff and bring up issues we could discuss in standup the next morning.

## Patterns!

### Action Items Not Getting Done

If action items aren't being done, the most common problems are that either they aren't tracked as part of normal sprint work, so they aren't top of mind for anyone, or the action items aren't concrete and actionable enough to actually get done.

For the first case, as recommended early, I strongly suggest converting all action items to tickets at the top of the next sprint, so they are given the first class "normal day-to-day work" experience.

For the latter case, I'd recommend applying the same standards you apply to your other engineering tickets to the action items from retros. That means clear context and well-defined acceptance criteria. Following the same principles as normal tickets in a sprint, the more well-defined the scope and AC, and the smaller the scope of the ticket, the easier it is to start and complete.

### Tickets Rolling Over
There are a lot of reasons velocity may be stuck and tickets are rolling over sprint to sprint. I'll cover some other bottlenecks below, but one of the first things to cover is to make sure your tickets are adequately groomed / refined and ready to go.

If tickets are too big and poorly defined, it's hard to ever "finish" them. In general, the smaller the ticket, the mor explicit the scope, the more clear the problem context, the explicitness of the acceptance criteria, etc. all contribute to being able to actually execute and delivering some value. My recommendations to teams struggling with grooming tend to have two aspects
1) What is a "Groomed Ticket"
2) What is the process for grooming

First, I'd recommend just getting the team to agree to some minimum requirement for a groomed ticket. For example, every Story needs 
- User Stories: given in an `As <persona>, I want <X> so <reason>`. Again, this isn't supposed to replace building relationships with customers/stakeholders and having discussions, but having the context provided on each ticket from the perspective of the effected user is critical.
- Acceptance Criteria: often (but not always) written in a testing format (Given/When/Then). In my ideal world, the ticket is "done" when the AC is translated into tests and the tests pass, pending other change management requirements.

This is just a starting point, of course. More than that, this is just my habit for defining tickets. Your teams will evolve their "templates" their own ways, but this seems like a great place to start. Some of my teams are experimenting with [Dual Track Agile](https://www.productboard.com/glossary/dual-track-agile/), so we have separate templates for tasks (delivery track, unambiguous and heavily focused on implementation details) and stories (discovery track, focused on the problem context, experimentation, learning, and eventually generating the delivery track tickets if the feature isn't killed).

Additionally, I highly recommend checking out [this talk which has some spicy takes on the current state of agile (timestamped to a bit about user stories).](https://youtu.be/ZrBQmIDdls4?t=2273). [That talk pairs well with this calmer discussion.](https://www.youtube.com/watch?v=KtHQGs3zFAM)

Not all teams do pointing, but if you do, I strongly recommend doing planning poker (and there are some fun [async slack apps](https://pelotoncycle.slack.com/apps/A57FFS3QE-poker-planner) to facilitate this). If any team member doesn't think they can point the ticket based exclusively on the content of the ticket, it's probably not refined enough to be actionable.

In terms of process, I'd start with a once a week, 1-hour grooming session, where the team collectively (and cross-functionally! It's important that product / onsite customers, etc. all be present and contributing) to make sure there is a clear prioritized block of tickets ready to be picked up in future sprints.

Note that this should be engineer-driven, not dictated by product, though collaboration with product is essential. And remember, priorities change! The backlog is likely to be constantly shifting and what was pointed one day may no longer be accurate on the next day, so don't be afraid to review already groomed tickets and reprioritize.

As the team builds competency in grooming, having a one off meeting each week becomes increasingly tedious, and you will likely want to explore getting rid of the meeting and doing grooming more "continuously." This requires experimentation, but ideally everyone on the team is engaged in taking looks at the backlog, bringing up epics/stories to discuss and reprioritize, etc.

Hopefully, by the time planning comes along, all you have to do is pull in the top X points, discuss themes/sprint goals and maybe do some light curating of the tickets in the sprint, review them together to make sure everyone is on the same page, and BAM -> planning only takes 10 minutes and everyone is confident in the work.

When tickets are well-defined and small, you tend to make more accurate guesses for what work can be done each sprint (on some of my teams, we decided 5s were "too big" and always tried to break them down as much as possible without losing the "cohesion" of the problem they were solving. In general, we found that 5s tended to break down in to 6-10 points worth of 2 and 3 pointers).

Similarly, well groomed tickets are easier to finish, so just by investing in grooming, you will see big gains in velocity and less rollover.

### Bottlenecks in Codereview

Codereview is a common bottleneck for new teams. You have to explain every decision you made when tackling a ticket, and convince someone who wasn't present for any of it that you made optimal decisions. They didn't see the things you tried that didn't work, or the conversations you had to narrow down solutions. And it only gets worse if the PRs are big. Reviewing someone elses' PR is more a or less a trust exercise. It's very difficult to just look at a diff and be like "yup I totally understand the context of all of this is and this is perfect."

Often times, this "handoff" results in thrashing as a review spends a while looking at it, leaves a bunch of comments that may or may not actually be helpful, and then it goes back to the dev, back to the reviewer, etc. The fewer times that happens the better.

And most engineers don't just wait for the codereview to get done (especially at organizations where this takes days or hours, instead of minutes), so they are working on something else and now there is the added difficultly of context switching and interruptions.

If this sounds like you, I'd recommend experimenting with pair programming: the "continuous code review." Two or more people working on a PR together is guaranteed better than one person making the changes and another person reviewing it after. It's more efficient, there are no hand-offs, discussions happen continuously, and it often results in better code from the start. You also avoid fun situations where a junior engineer spends days/weeks (oof) working on a change and then a senior engineer rejects it out of hand because it wrecks the whole architecture and it needs to be completely redone. If they were pairing from the start, the junior engineer learns much earlier on not to go down that rabbit-hole and everyone is happier. It feels bad to be put in the position where you have to reject someone's hard work out of hand.

If your organization change management process is ok with it, I'd suggest allowing for paired PRs to merge immediately after tests pass, though of course you should still circulate the PR to allow other's the opportunity to stay informed or make suggestions, even if they are after merging a PR. You can always revert it or do a follow-up if someone brings up something smart two hours later!

### Bottlenecks in UAT/Testing/QA

*Note, these terms (UAT, Testing, QA, etc.) are a little ambiguous and the differences are not always clear, but I'm just going to ignore that for now as I think everyone can understand what part of the process this is describing.

Once code is flowing, the next place tickets tend to bottleneck is in the UAT and QA/Testing stage.

On the ecommerce team at Peloton, we require that applicable tickets (read: anything that changes production behavior visible to users/customers in production, i.e. not refactoring) requires production signoff. All tickets that resulted in code changing requires "testing" signoff.

Most teams struggle with this because PMs are really busy and it's hard for them to be on top of everything all the time. Depending on the ticket, sometimes we allow engineering managers to do product sign off.

Some things we have done in retros to help with this are
1) Write simple testing instructions in the ticket when it is ready for PM review, explaining the workflow and how to test, so the PM can signoff async. If the ticket is simple enough, maybe you just want to link example orders/records/whatever that they can verify after, or you can take screenshots.
2) On another team, we introduced the concept of a "mini retro" at 4:50 each day when we could first quickly bring up any new issues/blockers we wanted to write down before standup the next day, but also we could use the last 5-10 minutes of the day to demo any of the work we got done. That way the whole team could actually see what was being accomplished and product could sign off on the spot, with only a 15-minute daily commitment.

For QA/Testing, we were running into issues where the "tester," being another handoff, need to be retold all the decisions and context for the change, shown the workflow, shown the unit tests, shown the implementation, etc. This was a huge bottleneck.

Some things that worked well for us were
1) Decided that instead of requiring signoff from "the SDET," instead any engineer who didn't make a commit could signoff, but the SDET would work with the engineers to teach them what the standards were for testing.
2) Automated testing was sufficient for signoff, so we pushed hard on TDD and including automated testing in the "definition of done" for tickets. This led to a better understanding of testing amongst engineers and as a side effect, better code! [Easy to test code is usually well architected.](../an_opinionated_set_of_testing_patterns/README.md)

### Too many meetings
A common complaint is that engineers have too many meetings to actually have time to do dev work. In order to address this problem, we usually try to figure out what kinds of meetings these are

If they are internal team meetings, like sprint ceremonies, these can be gradually shortened and optimized over time as the team builds competency and mastery of its own practices. For instance, once a team gets good at continuous grooming, it no longer needs a recurring grooming meeting. Once the backlog is in good shape, planning meetings take around 15 minutes. Additionally, since the team owns these meetings, they can just cancel them if they feel it delivers less value than just having more dev time.

If they are external team meetings, like working groups or TAGs or even just checkins on programs or meetings set up to talk about issues that other teams need help for, that's a lot trickier to solve.

I've seen some people try "no meetings Wednesdays" or stuff like that, but it always seems to fall apart after a few weeks. And honestly, I'm not sure if that does anything but fill up the other days with Wednesday's meetings.

What I have been experimenting with, to some successful, is to have the team block off an agreed upon block to "be available" together for dev work. If possible, I would make that time block auto-decline meetings. If its actually important, the organizer will reach out to ask if you can join. This way team can reclaim an hour or two to all be present and doing work together each day. You can tweek as you like over the course of multiple retros.

That being said, I think "meeting culture" deserves its own blog post. Meetings are definitely a useful tool, but in my opinion, maybe like 25% of the meetings I go to are actually good uses of synchronous time being blocked off. And once you start having a bunch of those meetings on repeat every week, the problem snowballs. If everyone is in so many meetings you can't schedule time with the people you need to talk to, when you do get a time, it starts to make sense to schedule recurring "checkins" with people just so you can talk to them at all. The problem compounds rapidly.


## Principles
From those patterns, you can see that most solutions I like involve one or more of
1) Can we do it more continuously? (instead of in big, synchronous chunks?)
2) Can we be more cross-functional? (and reduce thrashing from external dependencies)
3) Can we automate it? (and reduce manual process)
4) Can we foster more ad hoc conversations? (and reduce meetings)
5) Can we encourage teaching and knowledge sharing? (and reduce silos)
6) Can we reduce dependencies on individuals? (and take on work as a team, regardless of who is on vacation or otherwise busy)
7) Can we have more fun?
8) Can we learn more?
9) Can we minimize our processes over time?
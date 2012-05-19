/**
 * 
 */
package com.manning.sbia.ch08;

import org.junit.Test;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.context.RepeatContextSupport;
import org.springframework.batch.repeat.support.RepeatTemplate;

/**
 * @author acogoluegnes
 *
 */
public class RepeatTemplateTest {

	@Test public void callbackInternalCount() {
		RepeatTemplate repeatTemplate = new RepeatTemplate();
		repeatTemplate.iterate(new RepeatCallback() {
			
			int count = 0;
			
			@Override
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {
				count++;
				return count > 5 ? RepeatStatus.FINISHED : RepeatStatus.CONTINUABLE;
			}
		});
	}
	
	@Test public void callbackContextCount() {
		RepeatTemplate tpl = new RepeatTemplate();
		tpl.iterate(new RepeatCallback() {
			
			@Override
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {
				Integer count = (Integer) context.getAttribute("count");
				if(count == null) {
					count = 0;
				}
				count++;
				context.setAttribute("count",count);
				return count > 5 ? RepeatStatus.FINISHED : RepeatStatus.CONTINUABLE;
			}
		});
	}
	
	@Test public void completionStrategy() {
		RepeatTemplate tpl = new RepeatTemplate();
		tpl.setCompletionPolicy(new CompletionPolicy() {
			
			@Override
			public void update(RepeatContext context) {
				Integer count = (Integer) context.getAttribute("count");
				count++;
				context.setAttribute("count", count);
			}
			
			@Override
			public RepeatContext start(RepeatContext parent) {
				RepeatContextSupport ctx = new RepeatContextSupport(parent);
				ctx.setAttribute("count", 0);
				return ctx;
			}
			
			@Override
			public boolean isComplete(RepeatContext context, RepeatStatus result) {
				Integer count = (Integer) context.getAttribute("count");
				return count > 5;
			}
			
			@Override
			public boolean isComplete(RepeatContext context) {
				return isComplete(context, null);
			}
		});
		
		tpl.iterate(new RepeatCallback() {
			
			@Override
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {
				return RepeatStatus.CONTINUABLE;
			}
		});
	}
	
}

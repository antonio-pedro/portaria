import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OMDetailComponent } from './om-detail.component';

describe('OM Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OMDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OMDetailComponent,
              resolve: { oM: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OMDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load oM on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OMDetailComponent);

      // THEN
      expect(instance.oM).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

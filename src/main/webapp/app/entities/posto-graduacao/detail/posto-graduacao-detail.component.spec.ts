import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PostoGraduacaoDetailComponent } from './posto-graduacao-detail.component';

describe('PostoGraduacao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostoGraduacaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PostoGraduacaoDetailComponent,
              resolve: { postoGraduacao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PostoGraduacaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load postoGraduacao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PostoGraduacaoDetailComponent);

      // THEN
      expect(instance.postoGraduacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
